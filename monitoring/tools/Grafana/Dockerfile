FROM grafana/grafana

COPY configuration/grafana.ini /etc/grafana/
COPY configuration/dashboards.yaml /etc/grafana/provisioning/dashboards/
COPY configuration/datasources.yaml /etc/grafana/provisioning/datasources/
COPY configuration/dashboards /var/lib/grafana/dashboards/
COPY configuration/plugins /var/lib/grafana/plugins 

ADD replaceEnvVariables.sh /replaceEnvVariables.sh

RUN ls /etc/grafana/provisioning/datasources/
RUN ls /

USER root

RUN chown grafana: /etc/grafana/provisioning/datasources/
RUN chmod u+w /etc/grafana/provisioning/datasources/
RUN chown grafana: /var/lib/grafana/dashboards/
RUN chmod u+w /var/lib/grafana/dashboards/

RUN chown grafana /replaceEnvVariables.sh
RUN chmod 777 /replaceEnvVariables.sh

USER grafana

ENTRYPOINT ["/replaceEnvVariables.sh"]