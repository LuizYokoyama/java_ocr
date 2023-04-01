FROM openjdk:17-alpine

# Set the name of the jar
ENV APP_FILE ocr-0.0.1-SNAPSHOT.jar

ADD https://chromedriver.storage.googleapis.com/110.0.5481.77/chromedriver_linux64.zip /tmp/

RUN unzip /tmp/chromedriver_linux64.zip

# Open the port
EXPOSE 8080

# Copy our JAR
COPY target/$APP_FILE /app.jar

# Launch the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar" ]