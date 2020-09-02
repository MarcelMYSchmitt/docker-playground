var express = require("express");
const promClient = require('prom-client');
const request = require('request');
var sleep = require('system-sleep');

const SENTRY_BASE_URL = process.env.SENTRY_BASE_URL;
checkIfNullOrEmpty('SENTRY_BASE_URL', SENTRY_BASE_URL);

const metricServer = express();
const register = new promClient.Registry();

const gauge = new promClient.Gauge({ name: 'SENTRY_AVAILABILITY', help: 'Shows Availability of SENTRY.' });
register.registerMetric(gauge);

metricServer.get('/metrics', (req, res) => {
  res.set('Content-Type', register.contentType);
  res.end(register.metrics());
});


console.log('Server listening to 3001, metrics exposed on "/metrics" endpoint');
metricServer.listen(3002)


while (1) {
  CallEndpointAndSetAvailabilityMetric();
  sleep(3000);
}


// CHECK ENV VARIABLES
function checkIfNullOrEmpty(parameterName, parameterValue) {
  if (!parameterValue) {
    throw new Error(`${parameterName} is null or empty!`);
  }

  console.log(`${parameterName}: ${parameterValue}`);
}


// CALL AND SET SENTRY AVAILABILITY METRIC
function CallEndpointAndSetAvailabilityMetric() {

  const SENTRY_HEALTH_URL = `${SENTRY_BASE_URL}/_health/?full`
  console.log(`SENTRY_HEALTH_URL: ${SENTRY_HEALTH_URL}`)
  
  try {

    request(SENTRY_HEALTH_URL, (err, res, body) => {
      if (err) { 
        return console.log(err); 
        }

      console.log(body);
      
      var jsonObjet = JSON.parse(body);
      console.log(jsonObjet);

      if (jsonObjet.healthy.WarningStatusCheck && jsonObjet.healthy.CeleryAppVersionCheck && jsonObjet.healthy.CeleryAliveCheck) {
        console.log(`sentry_warning_status_check: ${jsonObjet.healthy.WarningStatusCheck} `);
        console.log(`sentry_celery_app_version_check: ${jsonObjet.healthy.CeleryAppVersionCheck} `);
        console.log(`sentry_celery_alive_check: ${jsonObjet.healthy.CeleryAliveCheck} `);

        console.log("Set Status of Metric to 1");
        gauge.set(1);
      } else {

        console.log("Set Status of Metric to 0");
        gauge.set(0);
      }    });  
    
  } catch{

    console.log("Set Status of Metric to 0");

    gauge.set(0);
  }
}