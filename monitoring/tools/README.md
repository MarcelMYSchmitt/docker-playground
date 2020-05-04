## How to run 
Before starting please make sure you have valid entries in your .env files. If it does not exist, please create it.

It should contain:  
PROMETHEUS_URL=http://localhost:9090  

We can later extend this config file for more env variables like other datasources (e.g. Azure Monitor or App Insights and so on).

To run all services within the script use `docker-compose up`.  
All valiables from the .env will be used, docker container will be built and started.  
To run one specific service use `docker-compose up 'servicename'`.  
If you change anything in the scripts please use `docker-compose up --build` for building a new version of the image.


You can acces Prometheus via http://localhost:9090 and Grafana via http://localhost:3000. 


## Provisioning of dashboards and datasources
You can provide dashboards and datasources from the beginning by loading these from specific folders. For dashboards and datasources there are yaml files which define where dashboards are stored or what kind of datasources will be used. Every dashboard in Grafana has its own file. If you want to update dashboards in Grafana you can export them as file or as json and save them in the 'provisioning' folder. Big advantage is that you do not need any volumes in your running Grafana Docker envrionment. 