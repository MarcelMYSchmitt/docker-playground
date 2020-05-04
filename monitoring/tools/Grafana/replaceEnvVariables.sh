#!/bin/bash
#datasources
sed -i -e "s@{{PrometheusUrl}}@$PrometheusUrl@g" /etc/grafana/provisioning/datasources/datasources.yaml

#dashboards
#sed -i -e "s/\${DS_PROMETHEUS}/Prometheus/g" /var/lib/grafana/dashboards/*
/run.sh
