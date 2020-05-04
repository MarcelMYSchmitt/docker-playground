package com.schmitt.playground.meterbinder;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

@Component
public class MatomoStatsProbe implements MeterBinder{
    
    private final AtomicLong matomoTrackerApiAvailabilityStatus = new AtomicLong();

    private final AtomicLong matomoReportingApiAvailabilityStatus = new AtomicLong();

    @Override
    public void bindTo(MeterRegistry registry) {
        Gauge.builder("matomo_tracker_api_availability", this, value -> matomoTrackerApiAvailabilityStatus.get())
                .description("status of matomo tracker api")
                .baseUnit("up")
                .register(registry);
        Gauge.builder("matomo_reporting_api_availability", this, value -> matomoReportingApiAvailabilityStatus.get())
        .description("status of matomo reporting api")
        .baseUnit("up")
        .register(registry);
    }
    
    public void setMatomoTrackerApiAvailabilityStatus(int value) {
        this.matomoTrackerApiAvailabilityStatus.set(value);
    }
    
    public long getMatomoTrackerApiAvailabilityStatus() {
    	return this.matomoTrackerApiAvailabilityStatus.get();
    }
   
    public void setMatomoReportingApiAvailabilityStatus(int value) {
        this.matomoReportingApiAvailabilityStatus.set(value);
    }
    
    public long getMatomoReportingApiAvailabilityStatus() {
    	return this.matomoReportingApiAvailabilityStatus.get();
    }
}
