package com.schmitt.playground.meterbinder;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class ProducerStatsProbe {

    private final Counter producerMessageMonitoringCounter;
    private final Counter producerMessagePlayAppCounter;

    public ProducerStatsProbe(MeterRegistry registry) {
    	producerMessageMonitoringCounter = Counter
                .builder("producer_message_monitoring_counter")
                .description("Counter for monitoring messages sent by producer during runtime.")
                .baseUnit("total")
                .register(registry);
    	producerMessagePlayAppCounter = Counter
                .builder("producer_message_playapp_counter")
                .description("Counter for playapp  messages sent by producer during runtime.")
                .baseUnit("total")
                .register(registry);
    }

    public void incrementProducerMessageMonitoringCounter() {
    	producerMessageMonitoringCounter.increment();
    }
    
    public void incrementProducerMessagePlayAppCounter() {
    	producerMessagePlayAppCounter.increment();
    }
}