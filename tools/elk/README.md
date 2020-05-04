# Elastic stack (ELK)

Run the latest version of the  with Docker and Docker Compose.

By default, the stack exposes the following ports:
* 5000: Logstash TCP input
* 9200: Elasticsearch HTTP
* 9300: Elasticsearch TCP transport
* 5601: Kibana


### Bringing up the stack

Run all services by using following command:
```
docker-compose up
```

You can also run all services in the background (detached mode) by adding the `-d` flag to the above command.

If you are starting the stack for the very first time, please read the section below attentively.


### Cleanup

Elasticsearch data is persisted inside a volume by default.

In order to entirely shutdown the stack and remove all persisted data, use the following Docker Compose command:

```console
$ docker-compose down -v
```

---------------------------------------------

## Initial setup

### Setting up user authentication

The stack is pre-configured with the following **privileged** bootstrap user:

* user: *elastic*
* password: *changeme*


1. Initialize passwords for built-in users

   ```console
   docker-compose exec -T elasticsearch bin/elasticsearch-setup-passwords auto --batch
   ```

   Passwords for all 6 built-in users will be randomly generated. Take note of them.


2. Unset the bootstrap password (_optional_)

   Remove the `ELASTIC_PASSWORD` environment variable from the `elasticsearch` service inside the Compose file
   (`docker-compose.yml`). It is only used to initialize the keystore during the initial startup of Elasticsearch.


3. Replace usernames and passwords in configuration files

   Use the `kibana` user inside the Kibana configuration file (`kibana/config/kibana.yml`) and the `logstash_system` user
   inside the Logstash configuration file (`logstash/config/logstash.yml`) in place of the existing `elastic` user.

   Replace the password for the `elastic` user inside the Logstash pipeline file (`logstash/pipeline/logstash.conf`).

   See also the Configuration section below.


4. Restart Kibana and Logstash to apply changes

   ```
   docker-compose restart kibana logstash
   ```


### Injecting data

Give Kibana about a minute to initialize, then access the Kibana web UI by hitting http://localhost:5601 with a web browser and use the following default credentials to log in:

* user: *elastic*
* password: *\<your generated elastic password>*

Now that the stack is running, you can go ahead and inject some log entries. The shipped Logstash configuration allows
you to send content via TCP:

```
cat /path/to/logfile.log | nc -q0 localhost 5000
```

```console
cat /path/to/logfile.log | nc -c localhost 5000
```

You can also load the sample data provided by your Kibana installation.


### Default Kibana index pattern creation

When Kibana launches for the first time, it is not configured with any index pattern.


#### Via the Kibana web UI

Navigate to the _Discover_ view of Kibana from the left sidebar. You will be prompted to create an index pattern. Enter
`logstash-*` to match Logstash indices then, on the next page, select `@timestamp` as the time filter field. Finally,
click _Create index pattern_ and return to the _Discover_ view to inspect your log entries.

Refer to `Connect Kibana with Elasticsearch` and `Creating an index pattern` for detailed instructions about the index pattern configuration.


#### On the command line

Create an index pattern via the Kibana API:

```
curl -XPOST -D- 'http://localhost:5601/api/saved_objects/index-pattern' \
    -H 'Content-Type: application/json' \
    -H 'kbn-version: 7.6.0' \
    -u elastic:<your generated elastic password> \
    -d '{"attributes":{"title":"logstash-*","timeFieldName":"@timestamp"}}'
```

The created pattern will automatically be marked as the default index pattern as soon as the Kibana UI is opened for the first time.

---------------------------------

## Configuration

### How to configure Elasticsearch

The Elasticsearch configuration is stored in `elasticsearch/config/elasticsearch.yml`.

You can also specify the options you want to override by setting environment variables inside the Compose file:

```
elasticsearch:

  environment:
    network.host: _non_loopback_
    cluster.name: my-cluster
```

Please refer to the following documentation page for more details about how to configure Elasticsearch inside Docker containers: `Install Elasticsearch with Docker`.


### How to configure Kibana

The Kibana default configuration is stored in `kibana/config/kibana.yml`.

It is also possible to map the entire `config` directory instead of a single file.

Please refer to the following documentation page for more details about how to configure Kibana inside Docker containers: `Running Kibana on Docker`.


### How to configure Logstash

The Logstash configuration is stored in `logstash/config/logstash.yml`.

It is also possible to map the entire `config` directory instead of a single file, however you must be aware that Logstash will be expecting a `log4j2.properties` file for its own logging.

Please refer to the following documentation page for more details about how to configure Logstash inside Docker containers: `Configuring Logstash for Docker`.


### How to disable paid features

Switch the value of Elasticsearch's `xpack.license.self_generated.type` option from `trial` to `basic` (see `License
settings`.


### How to scale out the Elasticsearch cluster

Follow the instructions from the Wiki: https://github.com/deviantony/docker-elk/wiki/Elasticsearch-cluster

---------------------------------

## Extensibility

### How to add plugins

To add plugins to any ELK component you have to:

1. Add a `RUN` statement to the corresponding `Dockerfile` (eg. `RUN logstash-plugin install logstash-filter-json`)
2. Add the associated plugin code configuration to the service configuration (eg. Logstash input/output)
3. Rebuild the images using the `docker-compose build` command

### How to enable the provided extensions

A few extensions are available inside the `extensions`directory. These extensions provide features which
are not part of the standard Elastic stack, but can be used to enrich it with extra integrations.

The documentation for these extensions is provided inside each individual subdirectory, on a per-extension basis. Some
of them require manual changes to the default ELK configuration.

---------------------------------

## JVM tuning

### How to specify the amount of memory used by a service

By default, both Elasticsearch and Logstash start with [1/4 of the total host
memory](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/parallel.html#default_heap_size) allocated to
the JVM Heap Size.

The startup scripts for Elasticsearch and Logstash can append extra JVM options from the value of an environment
variable, allowing the user to adjust the amount of memory that can be used by each component:

| Service       | Environment variable |
|---------------|----------------------|
| Elasticsearch | ES_JAVA_OPTS         |
| Logstash      | LS_JAVA_OPTS         |

To accomodate environments where memory is scarce (Docker for Mac has only 2 GB available by default), the Heap Size
allocation is capped by default to 256MB per service in the `docker-compose.yml` file. If you want to override the
default JVM configuration, edit the matching environment variable(s) in the `docker-compose.yml` file.

For example, to increase the maximum JVM Heap Size for Logstash:

```
logstash:

  environment:
    LS_JAVA_OPTS: -Xmx1g -Xms1g
```

### How to enable a remote JMX connection to a service

As for the Java Heap memory (see above), you can specify JVM options to enable JMX and map the JMX port on the Docker
host.

Update the `{ES,LS}_JAVA_OPTS` environment variable with the following content (I've mapped the JMX service on the port
18080, you can change that). Do not forget to update the `-Djava.rmi.server.hostname` option with the IP address of your
Docker host (replace **DOCKER_HOST_IP**):

```
logstash:

  environment:
    LS_JAVA_OPTS: -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=18080 -Dcom.sun.management.jmxremote.rmi.port=18080 -Djava.rmi.server.hostname=DOCKER_HOST_IP -Dcom.sun.management.jmxremote.local.only=false
```

## Going further

### Plugins and integrations

See the following Wiki pages:

* External applications: https://github.com/deviantony/docker-elk/wiki/External-applications
* Popular integrations: https://github.com/deviantony/docker-elk/wiki/Popular-integrations
* elk-stack: https://www.elastic.co/elk-stack
* stack-features: https://www.elastic.co/products/stack
* paid-features: https://www.elastic.co/subscriptions
* trial-license: https://www.elastic.co/guide/en/elasticsearch/reference/current/license-settings.html
* linux-postinstall: https://docs.docker.com/install/linux/linux-postinstall/
* booststap-checks: https://www.elastic.co/guide/en/elasticsearch/reference/current/bootstrap-checks.html
* es-sys-config: https://www.elastic.co/guide/en/elasticsearch/reference/current/system-config.html
* win-shareddrives: https://docs.docker.com/docker-for-windows/#shared-drives
* mac-mounts: https://docs.docker.com/docker-for-mac/osxfs/
* builtin-users: https://www.elastic.co/guide/en/elasticsearch/reference/current/built-in-users.html
* ls-security: https://www.elastic.co/guide/en/logstash/current/ls-security.html
* sec-tutorial: https://www.elastic.co/guide/en/elasticsearch/reference/current/security-getting-started.html
* connect-kibana: https://www.elastic.co/guide/en/kibana/current/connect-to-elasticsearch.html
* index-pattern: https://www.elastic.co/guide/en/kibana/current/index-patterns.html
* config-es: ./elasticsearch/config/elasticsearch.yml
* config-kbn: ./kibana/config/kibana.yml
* config-ls: ./logstash/config/logstash.yml
* es-docker: https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html
* kbn-docker: https://www.elastic.co/guide/en/kibana/current/docker.html
* ls-docker: https://www.elastic.co/guide/en/logstash/current/docker-config.html
* log4j-props: https://github.com/elastic/logstash/tree/7.6/docker/data/logstash/config
* esuser: https://github.com/elastic/elasticsearch/blob/7.6/distribution/docker/src/docker/Dockerfile#L23-L24
* upgrade: https://www.elastic.co/guide/en/elasticsearch/reference/current/setup-upgrade.html
* swarm-mode: https://docs.docker.com/engine/swarm/