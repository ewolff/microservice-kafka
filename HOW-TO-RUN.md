# How to Run

This is a step-by-step guide how to run the example:

## Installation

* The example is implemented in Java. See
   https://www.java.com/en/download/help/download_options.xml . The
   examples need to be compiled so you need to install a JDK (Java
   Development Kit). A JRE (Java Runtime Environment) is not
   sufficient. After the installation you should be able to execute
   `java` and `javac` on the command line.

* The example run in Docker Containers. You need to install Docker
  Community Edition, see https://www.docker.com/community-edition/
  . You should be able to run `docker` after the installation.

* The example need a lot of RAM. You should configure Docker to use 4
  GB of RAM. Otherwise Docker containers might be killed due to lack
  of RAM. On Windows and macOS you can find the RAM setting in the
  Docker application under Preferences/ Advanced.
  
* After installing Docker you should also be able to run
  `docker-compose`. If this is not possible, you might need to install
  it separately. See https://docs.docker.com/compose/install/ .

## Build

Change to the directory `microservice-kafka` and run `./mvnw clean
package` or `mvnw.cmd clean package` (Windows). This will take a while:

```
[~/microservice-kafka/microservice-kafka]./mvnw clean package
....
[INFO] 
[INFO] --- maven-jar-plugin:2.6:jar (default-jar) @ microservice-kafka-invoicing ---
[INFO] Building jar: /Users/wolff/microservice-kafka/microservice-kafka/microservice-kafka-invoicing/target/microservice-kafka-invoicing-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:1.5.4.RELEASE:repackage (default) @ microservice-kafka-invoicing ---
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] microservice-kafka ................................. SUCCESS [  1.191 s]
[INFO] microservice-kafka-order ........................... SUCCESS [ 37.543 s]
[INFO] microservice-kafka-shipping ........................ SUCCESS [ 49.739 s]
[INFO] microservice-kafka-invoicing ....................... SUCCESS [ 48.479 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 02:17 min
[INFO] Finished at: 2017-09-08T13:43:15+02:00
[INFO] Final Memory: 48M/427M
[INFO] ------------------------------------------------------------------------
```

If this does not work:

* Ensure that `settings.xml` in the directory `.m2` in your home
directory contains no configuration for a specific Maven repo. If in
doubt: delete the file.

* The tests use some ports on the local machine. Make sure that no
server runs in the background.
In particular make sure the Kafka port 9092 and the HTTP port 8080 are
available. There might be other ports that are needed, too.

* Skip the tests: `./mvnw clean package -Dmaven.test.skip=true` or
  `mvnw.cmd clean package -Dmaven.test.skip=true` (Windows).

* In rare cases dependencies might not be downloaded correctly. In
  that case: Remove the directory `repository` in the directory `.m2`
  in your home directory. Note that this means all dependencies will
  be downloaded again.

## Run the containers

First you need to build the Docker images. Change to the directory
`docker` and run `docker-compose build`. This will download some base
images, install software into Docker images and will therefore take
its time:

```
[~/microservice-kafka/docker]docker-compose build 
...
Step 7/7 : CMD apache2ctl -D FOREGROUND
 ---> Using cache
 ---> af6e0b1495b4
Successfully built af6e0b1495b4
Successfully tagged mskafka_apache:latest
Removing intermediate container 1d59f8227b12
```

Afterwards the Docker images should have been created. They have the prefix
`mskafka`:

```
[~/microservice-kafka/docker]docker images 
REPOSITORY                                              TAG                 IMAGE ID            CREATED             SIZE
mskafka_invoicing                                       latest              1fddb3132141        43 seconds ago      214MB
mskafka_shipping                                        latest              7340d766ea6f        46 seconds ago      214MB
mskafka_order                                           latest              0f9848e55054        49 seconds ago      215MB
mskafka_kafkacat                                        latest              461e8b02bb99        12 days ago          113MB
mskafka_postgres                                        latest              2b2f4f035d6d        12 days ago          269MB
```

Now you can start the containers using `docker-compose up -d`. The
`-d` option means that the containers will be started in the
background and won't output their stdout to the command line:

```
[~/microservice-kafka/docker]docker-compose up -d
Starting mskafka_zookeeper_1 ... 
Starting mskafka_postgres_1 ... 
Starting mskafka_zookeeper_1
Starting mskafka_zookeeper_1 ... done
Starting mskafka_kafka_1 ... 
Starting mskafka_kafka_1 ... done
Recreating mskafka_order_1 ... 
Recreating mskafka_invoicing_1 ... 
Recreating mskafka_invoicing_1
Recreating mskafka_shipping_1 ... 
Recreating mskafka_order_1
Recreating mskafka_invoicing_1 ... done
Recreating mskafka_apache_1 ... 
Recreating mskafka_apache_1 ... done
```

During the first start the Docker images for Zookeeper and Kafka will be downloaded during this step.

Check wether all containers are running:

```
[~/microservice-kafka/docker]docker ps
CONTAINER ID        IMAGE                          COMMAND                  CREATED             STATUS              PORTS                                                NAMES
a87d3671d7f0        mskafka_apache                 "/bin/sh -c 'apach..."   6 minutes ago       Up 6 minutes        0.0.0.0:8080->80/tcp                                 mskafka_apache_1
3efa376d06a2        mskafka_invoicing              "/bin/sh -c '/usr/..."   6 minutes ago       Up 6 minutes        8080/tcp                                             mskafka_invoicing_1
3dfe4b38d9d9        mskafka_order                  "/bin/sh -c '/usr/..."   6 minutes ago       Up 6 minutes        8080/tcp                                             mskafka_order_1
554109ba07a3        mskafka_shipping               "/bin/sh -c '/usr/..."   6 minutes ago       Up 6 minutes        8080/tcp                                             mskafka_shipping_1
b1ea3311f031        wurstmeister/kafka:0.10.2.1    "start-kafka.sh"         12 days ago         Up 6 minutes                                                             mskafka_kafka_1
c83247820e4d        mskafka_postgres               "docker-entrypoint..."   12 days ago         Up 6 minutes        5432/tcp                                             mskafka_postgres_1
a397c26c1947        wurstmeister/zookeeper:3.4.6   "/bin/sh -c '/usr/..."   12 days ago         Up 6 minutes        22/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp   mskafka_zookeeper_1
```
`docker ps -a`  also shows the terminated Docker containers. That is
useful to see Docker containers that crashed right after they started.

If one of the containers is not running, you can look at its logs using
e.g.  `docker logs mskafka_order_1`. The name of the container is
given in the last column of the output of `docker ps`. Looking at the
logs even works after the container has been
terminated. If the log says that the container has been `killed`, you
need to increase the RAM assigned to Docker to e.g. 4GB. On Windows
and macOS you can find the RAM setting in the Docker application under
Preferences/ Advanced.
  
If you need to do more trouble shooting open a shell in the container
using e.g. `docker exec -it mskafka_catalog_1 /bin/sh` or execute
command using `docker exec mskafka_catalog_1 /bin/ls`.

You can now go to http://localhost:8080/ and enter an order. That will
create a shipping and an invoice in the other two microservices.

You can terminate all containers using `docker-compose down`.
You can remove the stopped containers using `docker-compose rm`.
So these two command give you a clean new start of the system.
