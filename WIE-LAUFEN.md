# Beispiel starten

Die ist eine Schritt-für-Schritt-Anleitung zum Starten der Beispiele.
Informationen zu Maven und Docker finden sich im
[Cheatsheet-Projekt](https://github.com/ewolff/cheatsheets-DE).

## Installation

* Die Beispiele sind in Java implementiert. Daher muss Java
  installiert werden. Die Anleitung findet sich unter
  https://www.java.com/en/download/help/download_options.xml . Da die
  Beispiele kompiliert werden müssen, muss ein JDK (Java Development
  Kit) installiert werden. Das JRE (Java Runtime Environment) reicht
  nicht aus. Nach der Installation sollte sowohl `java` und `javac` in
  der Eingabeaufforderung möglich sein.

* Die Beispiele laufen in Docker Containern. Dazu ist eine
  Installation von Docker Community Edition notwendig, siehe
  https://www.docker.com/community-edition/ . Docker kann mit
  `docker` aufgerufen werden. Das sollte nach der Installation ohne
  Fehler möglich sein.

* Die Beispiele benötigen zum Teil sehr viel Speicher. Daher sollte
  Docker ca. 4 GB zur Verfügung haben. Sonst kann es vorkommen, dass
  Docker Container aus Speichermangel beendet werden. Unter Windows
  und macOS findet sich die Einstellung dafür in der Docker-Anwendung
  unter Preferences/ Advanced.

* Nach der Installation von Docker sollte `docker-compose` aufrufbar
  sein. Wenn Docker Compose nicht aufgerufen werden kann, ist es nicht
  als Teil der Docker Community Edition installiert worden. Dann ist
  eine separate Installation notwendig, siehe
  https://docs.docker.com/compose/install/ .

## Build

Wechsel in das Verzeichnis `microservice-kafka` und starte
`./mvnw clean package` bzw. `mvnw.cmd clean package`. Das wird einige
Zeit dauern:

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

Weitere Information zu Maven gibt es im
[Maven Cheatsheet](https://github.com/ewolff/cheatsheets-DE/blob/master/MavenCheatSheet.md).

Falls es dabei zu Fehlern kommt:

* Stelle sicher, dass die Datei `settings.xml` im Verzeichnis  `.m2`
in deinem Heimatverzeichnis keine Konfiguration für ein spezielles
Maven Repository enthalten. Im Zweifelsfall kannst du die Datei
einfach löschen.

* Die Tests nutzen einige Ports auf dem Rechner. Stelle sicher, dass
  im Hintergrund keine Server laufen.
  Vor allem muss der Kafka-Port 9092 und der HTTP-Port 8080
  verfügbar sein. Gegebenenfalls sind auch noch andere
  Ports notwendig.


* Führe die Tests beim Build nicht aus: `./mvnw clean package
  -Dmaven.test.skip=true` bzw. `mvnw.cmd clean package
  -Dmaven.test.skip=true` (Windows).

* In einigen selten Fällen kann es vorkommen, dass die Abhängigkeiten
  nicht korrekt heruntergeladen werden. Wenn du das Verzeichnis
  `repository` im Verzeichnis `.m2` löscht, werden alle Abhängigkeiten
  erneut heruntergeladen.

## Docker Container starten

Weitere Information zu Docker gibt es im
[Docker Cheatsheet](https://github.com/ewolff/cheatsheets-DE/blob/master/DockerCheatSheet.md).

Zunächst musst du die Docker Images bauen. Wechsel in das Verzeichnis 
`docker` und starte `docker-compose build`. Das lädt die Basis-Images
herunter und installiert die Software in die Docker Images:

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

Danach sollten die Docker Images erzeugt worden sein. Sie haben das
Präfix `mskafka`:

```
[~/microservice-kafka/docker]docker images 
REPOSITORY                                              TAG                 IMAGE ID            CREATED             SIZE
mskafka_invoicing                                       latest              1fddb3132141        43 seconds ago      214MB
mskafka_shipping                                        latest              7340d766ea6f        46 seconds ago      214MB
mskafka_order                                           latest              0f9848e55054        49 seconds ago      215MB
mskafka_kafkacat                                        latest              461e8b02bb99        12 days ago          113MB
mskafka_postgres                                        latest              2b2f4f035d6d        12 days ago          269MB
```

Nun kannst Du die Container mit `docker-compose up -d` starten. Die
Option `-d` bedeutet, dass die Container im Hintergrund gestartet
werden und keine Ausgabe auf der Kommandozeile erzeugen.

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

Wenn das System zum ersten Mal gestartet wird, werden noch einige
Docker Images heruntergeladen.

Du kannst nun überprüfen, ob alle Docker Container laufen:

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

`docker ps -a`  zeigt auch die terminierten Docker Container an. Das
ist nützlich, wenn ein Docker Container sich sofort nach dem Start
wieder beendet..

Wenn einer der Docker Container nicht läuft, kannst du dir die Logs
beispielsweise mit `docker logs mskafka_order_1` anschauen. Der Name
der Container steht in der letzten Spalte der Ausgabe von `docker
ps`. Das Anzeigen der Logs funktioniert auch dann, wenn der Container
bereits beendet worden ist. Falls im Log steht, dass der Container
`killed` ist, dann hat Docker den Container wegen Speichermangel
beendet. Du solltest Docker mehr RAM zuweisen z.B. 4GB. Unter Windows
und macOS findet sich die RAM-Einstellung in der Docker application
unter Preferences/ Advanced.

Um einen Container genauer zu untersuchen, kannst du eine Shell in dem
Container starten. Beispielsweise mit `docker exec -it
mskafka_catalog_1 /bin/sh` oder du kannst in dem Container ein
Kommando mit `docker exec mskafka_catalog_1 /bin/ls` ausführen.

Unter http://localhost:8080/ kannst du nun eine Bestellung
erfassen. Nach einiger Zeit sollte für die Bestellung eine Lieferung
und eine Rechnung erstellt worden sei.

Mit `docker-compose down` kannst du alle Container beenden.
Und mit `docker-compose rm` kannst du die beendeten container
löschen.
Zusammen ermöglichen diese beiden Kommandos also einen sauberen
Neustart des Systems.
