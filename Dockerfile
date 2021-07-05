# First stage: JDK 11 with modules required for Spring Boot
FROM centos:7 AS build

RUN echo 'Diego'
RUN yum repolist all
RUN yum update -y && yum install vim -y && yum install git -y && yum install wget -y && yum install unzip -y
COPY ./jdk-8u291-linux-x64.rpm /utils/jdk-8u291-linux-x64.rpm
RUN rpm -ivh /utils/jdk-8u291-linux-x64.rpm
RUN java -version
RUN wget https://downloads.gradle-dn.com/distributions/gradle-6.3-bin.zip
RUN unzip gradle-6.3-bin.zip
RUN mv gradle-6.3 gradle
RUN git clone https://github.com/slasheldiego/message-generator.git
WORKDIR message-generator
RUN ../gradle/bin/gradle clean fatJar
RUN mkdir /app
RUN cp /message-generator/build/libs/messages-generator-all-1.0.jar /app/message-generator.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap","-jar","/app/message-generator.jar"]
