package com.schmitt.playground.meterbinder;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class ConsumerStatsProbe {

	private final Counter consumerMessageMonitoringCounter;
	private final Counter consumerMessagePlayAppCounter;
	private final AtomicLong watchdogAvailabilityStatus = new AtomicLong();

	public ConsumerStatsProbe(MeterRegistry registry) {
		consumerMessageMonitoringCounter = Counter.builder("consumer_message_monitoring_counter")
				.description("Counter for monitoring messages consumed by consumer during runtime.").baseUnit("total")
				.register(registry);
		consumerMessagePlayAppCounter = Counter.builder("consumer_message_playapp_counter")
				.description("Counter for playapp  messages consumed by consumer during runtime.").baseUnit("total")
				.register(registry);
		Gauge.builder("watchdog_availability", this, value -> watchdogAvailabilityStatus.get())
				.description("status of core achitecture").baseUnit("up").register(registry);

	}

	public void incrementConsumerMessageMonitoringCounter() {
		consumerMessageMonitoringCounter.increment();
	}

	public void incrementConsumerMessagePlayAppCounter() {
		consumerMessagePlayAppCounter.increment();
	}

	public void setwatchdogAvailabilityStatus(int value) {
		this.watchdogAvailabilityStatus.set(value);
	}

	public long getwatchdogAvailabilityStatus() {
		return this.watchdogAvailabilityStatus.get();
	}
}
