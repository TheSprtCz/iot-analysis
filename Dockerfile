FROM maven:3.2-jdk-8
ADD . /
CMD mvn camel:run
