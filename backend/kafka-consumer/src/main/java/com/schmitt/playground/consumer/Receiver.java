package com.schmitt.playground.consumer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.schmitt.playground.meterbinder.ConsumerStatsProbe;
import com.schmitt.playground.model.PlayAppMessage;
import com.schmitt.playground.model.WatchdogMessage;

public class Receiver {

	@Autowired
	SimpMessagingTemplate template;

	@Autowired
	private ConsumerStatsProbe consumerStatsProbe;

	private CountDownLatch latch1 = new CountDownLatch(1);
	private CountDownLatch latch2 = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch1;
	}

	public CountDownLatch getLatch2() {
		return latch2;
	}

	@KafkaListener(topics = "${kafka.topic.monitoring}", containerFactory = "kafkaListenerWatchdogContainerFactory", groupId = "${kafka.topic.monitoring}")
	public void consume(@Payload WatchdogMessage message) throws IOException {
		System.out.println(String.format("#### -> Consumed message -> %s", message));

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
			Date parsedDate = dateFormat.parse(message.getTimestamp());

			Date currentDate = new Date();
			long seconds = (currentDate.getTime() - parsedDate.getTime()) / 1000;

			System.out.println("parsedDate: " + parsedDate.toString());
			System.out.println("currentDate: " + currentDate.toString());
			System.out.println("Difference seconds from monitoring message datime: " + seconds);

			if (seconds <= 30) {
				// 1 means ok
				consumerStatsProbe.setwatchdogAvailabilityStatus(1);
			} else {
				// 2 means slow
				consumerStatsProbe.setwatchdogAvailabilityStatus(2);
			}

			// count messages for monitoring
			consumerStatsProbe.incrementConsumerMessageMonitoringCounter();
		} catch (Exception e) {
			// 0 means down or error
			consumerStatsProbe.setwatchdogAvailabilityStatus(0);

			System.out.println("Error parsing seconds from monitoring watchdog message: " + e);
		}

		latch1.countDown();

	}

	@KafkaListener(topics = "${kafka.topic.playapp}", containerFactory = "kafkaListenerPlayAppContainerFactory", groupId = "${kafka.topic.playapp}")
	public void consume(@Payload PlayAppMessage message) throws IOException {
		System.out.println(String.format("#### -> Consumed message -> %s", message));

		// websockets
		// messages will be available via /ws/topic/messages
		template.convertAndSend("/topic/messages", message);

		// count messages for monitoring
		consumerStatsProbe.incrementConsumerMessagePlayAppCounter();

		latch2.countDown();
	}

}
