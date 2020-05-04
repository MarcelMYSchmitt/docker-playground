package com.schmitt.playground.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.schmitt.playground.consumer.Receiver;
import com.schmitt.playground.model.PlayAppMessage;
import com.schmitt.playground.model.WatchdogMessage;

@Configuration
@EnableKafka
public class ReceiverConfig {

	@Value(value = "${kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public Map<String, Object> consumerWatchDogConfigs() {

		if (bootstrapServers == null) {
			throw new NullPointerException("Kafka Server URL not set!");
		}

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "watchdog");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}

	@Bean
	public ConsumerFactory<String, WatchdogMessage> consumerWatchdogFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerWatchDogConfigs(), new StringDeserializer(),
				new JsonDeserializer<>(WatchdogMessage.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, WatchdogMessage> kafkaListenerWatchdogContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, WatchdogMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerWatchdogFactory());
		return factory;
	}
	
	
	@Bean
	public Map<String, Object> consumerPlayAppConfigs() {

		if (bootstrapServers == null) {
			throw new NullPointerException("Kafka Server URL not set!");
		}

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "frontend");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}

	@Bean
	public ConsumerFactory<String, PlayAppMessage> consumerPlayAppFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerPlayAppConfigs(), new StringDeserializer(),
				new JsonDeserializer<>(PlayAppMessage.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PlayAppMessage> kafkaListenerPlayAppContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, PlayAppMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerPlayAppFactory());
		return factory;
	}

	@Bean
	public Receiver receiver() {
		return new Receiver();
	}
}