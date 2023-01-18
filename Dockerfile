FROM ubuntu:22.10
RUN apt-get update

#install java
RUN apt-get install -y openjdk-17-jdk
ARG JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
ENV JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"

# Install tesseract library
RUN apt-get install -y tesseract-ocr
RUN mkdir -p /usr/share/tesseract-ocr/5/tessdata/
ADD https://github.com/tesseract-ocr/tessdata/blob/main/por.traineddata?raw=true /usr/share/tesseract-ocr/5/tessdata/por.traineddata
RUN chmod u=rwx,g=rwx,o=rwx /usr/share/tesseract-ocr/5/tessdata/por.traineddata

# Check the installation status
RUN tesseract --list-langs
RUN tesseract -v

# Set the name of the jar
ENV APP_FILE ocr-0.0.1-SNAPSHOT.jar

# Open the port
EXPOSE 8080

# Copy our JAR
COPY target/$APP_FILE /app.jar

# Launch the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar" ]