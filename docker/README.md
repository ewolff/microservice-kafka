Run Microservices Kafka Demo with Docker Compose and Docker Machine
==================================================

Docker Machine can create virtual machines to run Docker on. It
supports Virtual Box but also many other technologies including
several clouds. Docker Compose create all the Docker containers needed
fot the systems.

*THIS IS A DEMO. IT IS UNSECURE AND CONTAINS DEFAULT PASSWORDS AND PASSWORDS
IN CLEAN TEXT IN CONFIGURATION FILES BESIDES OTHER THINGS. IT IS MEANT TO BE
EASY TO UNDERSTAND.*

*DO NOT RUN THIS IN PRODUCTION!!!!!!*

To run the demo:

- Install Maven, see https://maven.apache.org/download.cgi
- Install Virtual Box from https://www.virtualbox.org/wiki/Downloads
- Install Docker Compose, see
https://docs.docker.com/compose/#installation-and-set-up
- Install Docker, see https://docs.docker.com/installation/
- Install Docker Machine, see https://docs.docker.com/machine/#installation
- Go to directory `microservice-kafka` and run `mvn package` there
- If you want to use Docker Machine:
  - Execute `docker-machine create  --virtualbox-memory "4096" --driver
    virtualbox dev` . This will create a new environment called `dev`with Docker
    Machine. It will be virtual machine in Virtual Box with 4GB RAM.
  - Execute `eval "$(docker-machine env dev)"` (Linux / Mac OS X). You
       might need to set your shell: `eval "$(docker-machine env --shell
       bash dev)"`. For Windows it's
      `docker-machine.exe env --shell powershell dev` (Windows,
      Powershell) /  `docker-machine.exe env --shell cmd dev` (Windows,
      cmd.exe). Now the docker tool will use the newly created virtual
      machine as environment.
- If you are running a local Docker installation on Windows or macOS,
  go to Preferences -> Advanced and make sure memory is set to 4 GB. 
- Change to the directory `docker` and run `docker-compose
   build`followed by `docker-compose up -d`. 


The result should be:

- Docker Compose builds the Docker images and runs them.
- Use `docker-machine ip dev` to find the IP adress of the virtual machine.
- You can access the Apache httpd at http://ipadresss:8080/ .  It
  provides  access to all microservices.

- Use `docker-machine rm dev` to destroy the virtual machine.
