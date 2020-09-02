# Introduction

This repository contains: 
 - complete ELK Stack
 - Matomo for User Activity Tracking
 - Sentry for User Error Tracking
 - Kafka as Message Broker
 - Kafka Producer and Consumer (both in Java)
 - Frontend (Angular) for displaying Kafka data, also for sending data to ELK and Matomo  
 - Monitoring tools (Prometheus, Prometheus AlertManager, Grafana)
 - Monitoring Exporter (Kafka, Matomo, ELK, Sentry)

This repository is just a simple playground for showing the functionalities of different services....these are not production ready. 
All services are dockerized and can be controlled by different docker compose files. 

Info: In the powerpoint presentation such as the screenshots Sentry is not mentioned. 

---

## How to run

EOL Problems: Due to linux / windows conversion problems please make sure you always have the correct otherwiese you could have problems building some services: `Use notepad++, go to edit -> EOL conversion -> change from CRLF to LF.`  


All services are using docker-compose files. There are also only Dockerfiles available. You can find a specific compose file in every folder.

Start using a service by `docker-compose up`. With `-d` you can start it in detached mode. If you change something in code you have to rebuild the specific container by using `--build` in your compose command. 

Info:  
In all docker-compose files we are exposing our ports to access them from localhost. This is just for development purposes. Normally you do not want this for all services.
For our monitoring we are using the docker network and docker service name functionality for scraping metrics.  
We can access the metrics of our kafka-consumer (written in Java) by using the http:localhost:8380/actuator/metrics endpoint for example.

For initial usage please create a local network via `docker network create playground-network`. This local network will be used for service name discovery. 

Services and Endpoints:

 - Matomo  
   After starting Matomo will be available under: http://localhost:7000/index.php

 - ELK  
   In docker-compose file such as the specific config files you can see some ENV Variables for setting authentication.
   Default values are `elastic` as username and `changeme`as password. If you want to use Kibana you have to login by using these credentials.
   If you want to send data to elasticsearch you also have to provide `changeme` as password.    
   After starting Kibana will be available under: http://localhost:5601

 - PlayApp Angular App  
   After starting Frontend will be available under: http://localhost:3100

 - Senty  
   After starting Sentry will be available under: http://localhost: 9000

 - Sentry Exporter  
   After starting Sentry will be available under: http://localhost:3002
 
 - Kafka  
   After starting Grafana will be available under: http://localhost:3000

 - Kafka Producer  
   Producer starts sending data to Kafka.  
   After starting Producer endpoints will be available under: http://localhost:8280

 - Kafka Consumer  
   Consumer starts consuming data from Kafka and provide these data to the Angular Frontend via Websockets.
   After starting Consumer endpoints will be available under: http://localhost:8380

 - Matomo Exporter  
   Retrieves status of Matomo by requesting api endpoints.
   After starting Matomo Exporter endpoints will be available under: http://localhost:9308

 - Kafka Exporter  
   After starting Matomo Exporter endpoints will be available under: http://localhost:8180

 - ElasticSearch - Logstash Exporter  
   After starting ElasticSearch Exporter endpoints will be available under: http://localhost:9114
   After starting Logstash Exporter endpoints will be available under: http://localhost:9304

 - Prometheus  
   After starting Prometheus will be available under: http://localhost:9090

 - Prometheus AlertManager
   After starting Prometheus AlertManager will be available under http://localhost:9093

 - Grafana  
   After starting Grafana will be available under: http://localhost:3000


---

## Basic Alerting

As you can see we have set up the Prometheus AlertManager within our Monitoring Tools. There you can find a simple configuration for using WebHooks as alerting communication way. There is also a basic alerting defined in our Dashboard in Grafana.

As defined above, basic alerting is also not production ready neither recommended to use for that scenario. In this example it just shows the differences between Grafana and Prometheus AlertManager Webhook configurations...in AlertManager you can define more things lie waiting conditions before sending Webhooks. In Grafana a Webhook will be sent immediately, in AlertManager after 30 seconds for example.s