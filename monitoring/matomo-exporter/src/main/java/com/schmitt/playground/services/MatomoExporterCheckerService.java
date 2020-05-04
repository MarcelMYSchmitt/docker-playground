package com.schmitt.playground.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.schmitt.playground.clients.MatomoExporterCheckerClient;
import com.schmitt.playground.meterbinder.MatomoStatsProbe;

@Component
public class MatomoExporterCheckerService {

	// default value is localhost, in docker we use service name discovery
	@Value("${matomoBaseUrl}")
	private String matomoBaseUrl;

	@Value("${matomoTrackerApiEndpoint}")
	private String matomoTrackerApiEndpoint;

	@Value("${matomoReportingApiEndpoint}")
	private String matomoReportingApiEndpoint;

	@Value("${matomoReportingApiAuthToken}")
	private String matomoReportingApiAuthToken;

	private MatomoStatsProbe matomoStatsProbe;

	@Autowired
	private MatomoExporterCheckerClient matomoExporterCheckerClient;

	public MatomoExporterCheckerService(MatomoStatsProbe matomoStatsProbe,
			MatomoExporterCheckerClient matomoExporterCheckerClient) {
		this.matomoStatsProbe = matomoStatsProbe;
		this.matomoExporterCheckerClient = matomoExporterCheckerClient;
	}

	/*
	 * 
	 * Call Matomo Tracking and Reporting API endpoints and set availability status
	 * depending on receiving status codes. Repeat every 5 seconds.
	 * 
	 */
	@Scheduled(fixedRate = 5000)
	public void setMatomoAvailability() {

		// call Tracker API
		String matomoTrackerApiUrl = matomoBaseUrl + matomoTrackerApiEndpoint;
		Integer matomoTrackerStatusCode = matomoExporterCheckerClient.callMatomoApiEndpoint(matomoTrackerApiUrl);

		if (matomoTrackerStatusCode == 200) {
			matomoStatsProbe.setMatomoTrackerApiAvailabilityStatus(1);

		} else {
			matomoStatsProbe.setMatomoTrackerApiAvailabilityStatus(0);
		}

		System.out.println("Matomo Tracker Api status: " + matomoStatsProbe.getMatomoTrackerApiAvailabilityStatus());

		// call Reporting API
		String matomoReportingApiUrl = matomoBaseUrl + matomoReportingApiEndpoint + matomoReportingApiAuthToken;
		Integer matomoReportingApiStatusCode = matomoExporterCheckerClient.callMatomoApiEndpoint(matomoReportingApiUrl);

		if (matomoReportingApiStatusCode == 200) {
			matomoStatsProbe.setMatomoReportingApiAvailabilityStatus(1);

		} else {
			matomoStatsProbe.setMatomoReportingApiAvailabilityStatus(0);
		}

		System.out
				.println("Matomo Reporting Api status: " + matomoStatsProbe.getMatomoReportingApiAvailabilityStatus());
	}
}
