# https://www.docker.elastic.co/
FROM docker.elastic.co/kibana/kibana:7.6.0

# Add your kibana plugins setup here
# Example: RUN kibana-plugin install <name|url>
RUN bin/kibana-plugin install https://github.com/pjhampton/kibana-prometheus-exporter/releases/download/7.6.0/kibana-prometheus-exporter-7.6.0.zip

