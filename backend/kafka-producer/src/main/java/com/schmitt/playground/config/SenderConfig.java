package com.schmitt.playground.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.schmitt.playground.Producer.Sender;
import com.schmitt.playground.model.PlayAppMessage;
import com.schmitt.playground.model.WatchdogMessage;

@Configuration
public class SenderConfig {

  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public Map<String, Object> producerConfigs() {
	  
	if (bootstrapServers == null) {
		throw new NullPointerException("Kafka Server URL not set!");
	}
	
    Map<String, Object> props = new HashMap<>();
    props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return props;
  }

  @Bean
  public ProducerFactory<String, WatchdogMessage> producerkafkaWatchdogTemplateFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaTemplate<String, WatchdogMessage> kafkaWatchdogTemplate() {
    return new KafkaTemplate<>(producerkafkaWatchdogTemplateFactory());
  }
  
  @Bean
  public ProducerFactory<String, PlayAppMessage> producerkafkakafkaPlayAppTemplateTemplateFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaTemplate<String, PlayAppMessage> kafkaPlayAppTemplateTemplate() {
    return new KafkaTemplate<>(producerkafkakafkaPlayAppTemplateTemplateFactory());
  }

  @Bean
  public Sender producer() {
    return new Sender();
  }
}
