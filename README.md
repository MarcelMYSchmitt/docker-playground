# Introduction

This repository contains: 
 - complete ELK Stack
 - Matomo for User Activity Tracking
 - Kafka as Message Broker
 - Kafka Producer and Consumer (both in Java)
 - Frontend (Angular) for displaying Kafka data, also for sending data to ELK and Matomo  
 - Monitoring tools (Prometheus, Grafana)
 - Monitoring Exporter (Kafka, Matomo, ELK)

This repository is just a simple playground for showing the functionalities of different services....these are not production ready. 
All services are dockerized and can be controlled by different docker compose files. 


## How tu run

All services are using docker-compose files. There are also only Dockerfiles available. You can find a specific compose file in every folder.


### General:  

Start using a service by `docker-compose up`. With `-d` you can start it in detached mode. 

 - Matomo  
   After starting Matomo will be available under: http://localhost:7000/index.php

 - ELK  
   In docker-compose file such as the specific config files you can see some ENV Variables for setting authentication.
   Default values are `elastic` as username and `changeme`as password. If you want to use Kibana you have to login by using these credentials.
   If you want to send data to elasticsearch you also have to provide `changeme` as password.    
   After starting Kibana will be available under: http://localhost:5601

 - PlayApp Angular App  
   After starting Frontend will be available under: http://localhost:3100

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

 - Grafana  
   After starting Grafana will be available under: http://localhost:3000

Info:  
In all docker-compose files we are exposing our ports to access them from localhost. This is just for development purposes. Normally you do not want this for all services.
For our monitoring services we could use the docker network and docker service name functionality. 

# TO DO

Extend Readme and setup.
Extend architecture diagram. 

