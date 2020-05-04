package com.schmitt.playground.clients;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MatomoExporterCheckerClient {

	private final RestTemplate restTemplate;

	public MatomoExporterCheckerClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	// call Matomo Api endpoint and return status code value, default if something
	// is not working = 500
	public Integer callMatomoApiEndpoint(String matomoApiUrlEndpoint) {

		try {
			ResponseEntity<String> entity = restTemplate.getForEntity(matomoApiUrlEndpoint, String.class);
			HttpStatus statusCode = entity.getStatusCode();
			return statusCode.value();

		} catch (Exception ex) {

			System.out.println("Error while calling Matomo Tracker Api Endpoint, is Matomo running?");
			System.out.println("Matomo Endpoint: " + matomoApiUrlEndpoint + " was called.");
			System.out.print("Error message: " + ex);
		}

		return 500;
	}

}