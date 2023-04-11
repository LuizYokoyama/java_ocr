FROM openjdk:17-jdk-alpine

# Set the name of the jar
ENV APP_FILE ocr-0.0.1-SNAPSHOT.jar

ADD https://github.com/LuizYokoyama/java_ocr/blob/qrcode_readed_url_firefox_driver/geckodriver/geckodriver?raw=true /geckodriver/geckodriver

RUN chmod o+x /geckodriver/geckodriver

RUN apk update
RUN apk add firefox

# Open the port
EXPOSE 8080

# Copy our JAR
COPY target/$APP_FILE /app.jar

# Launch the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar" ]