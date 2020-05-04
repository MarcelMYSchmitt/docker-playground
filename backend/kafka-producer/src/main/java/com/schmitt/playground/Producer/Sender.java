package com.schmitt.playground.Producer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.springframework.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.schmitt.playground.meterbinder.ProducerStatsProbe;
import com.schmitt.playground.model.PlayAppMessage;
import com.schmitt.playground.model.WatchdogMessage;

public class Sender {

	@Value("${kafka.topic.monitoring}")
	private String watchdogTopic;

	@Value("${kafka.topic.playapp}")
	private String playAppTopic;

	@Autowired
	private KafkaTemplate<String, WatchdogMessage> kafkaWatchdogTemplate;

	@Autowired
	private KafkaTemplate<String, PlayAppMessage> kafkaPlayAppTemplate;

	private Random random = new Random();

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

	@Autowired
	private ProducerStatsProbe producerStatsProbe;

	@Scheduled(fixedRate = 7500)
	public void sendWatchdogMessage() {

		if (watchdogTopic == null) {
			throw new NullPointerException("Kafka Topic for Watchdog not set!");
		}

		WatchdogMessage messageContent = new WatchdogMessage();
		messageContent.setId(1);
		messageContent.setMessage("Watchdog Message for checking availability.");
		messageContent.setTimestamp(sdf.format(new Timestamp(System.currentTimeMillis())));

		Message<WatchdogMessage> watchdogMessage = MessageBuilder.withPayload(messageContent)
				.setHeader(KafkaHeaders.TOPIC, watchdogTopic).build();

		ListenableFuture<SendResult<String, WatchdogMessage>> kafkaWatchdogFuture = kafkaWatchdogTemplate
				.send(watchdogMessage);

		kafkaWatchdogFuture.addCallback(new ListenableFutureCallback<SendResult<String, WatchdogMessage>>() {

			@Override
			public void onSuccess(SendResult<String, WatchdogMessage> result) {
				System.out.println(String.format("#### -> SENT message -> %s", watchdogMessage));
				
				// set Prometheus counter +1
				producerStatsProbe.incrementProducerMessageMonitoringCounter();
				System.out.println("Incremented Producer Message Counter for montitoring message sent.");
			}

			@Override
			public void onFailure(Throwable ex) {
				System.out.println(
						"#### -> Unable to send message=[" + watchdogMessage + "] due to : " + ex.getMessage());
			}
		});

	}

	@Scheduled(fixedRate = 5000)
	public void sendPlayAppMessage() {

		if (playAppTopic == null) {
			throw new NullPointerException("Kafka Topic for Playapp not set!");
		}

		PlayAppMessage messageContent = new PlayAppMessage();
		messageContent.setRandomId(random.nextInt());
		messageContent.setMessage("Play App Message for Frontend.");
		messageContent.setTimestamp(sdf.format(new Timestamp(System.currentTimeMillis())));

		Message<PlayAppMessage> playAppMessage = MessageBuilder.withPayload(messageContent)
				.setHeader(KafkaHeaders.TOPIC, playAppTopic).build();

		ListenableFuture<SendResult<String, PlayAppMessage>> kafkaPlayAppFuture = kafkaPlayAppTemplate
				.send(playAppMessage);

		kafkaPlayAppFuture.addCallback(new ListenableFutureCallback<SendResult<String, PlayAppMessage>>() {

			@Override
			public void onSuccess(SendResult<String, PlayAppMessage> result) {
				System.out.println(String.format("#### -> SENT message -> %s", playAppMessage));

				// set Prometheus counter +1
				producerStatsProbe.incrementProducerMessagePlayAppCounter();
				System.out.println("Incremented Producer Message Counter for play app message sent.");
			}

			@Override
			public void onFailure(Throwable ex) {
				System.out
						.println("#### -> Unable to send message=[" + playAppMessage + "] due to : " + ex.getMessage());
			}
		});

	}
}