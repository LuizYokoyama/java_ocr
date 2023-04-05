FROM openjdk:17-alpine

# Set the name of the jar
ENV APP_FILE ocr-0.0.1-SNAPSHOT.jar

ADD https://github.com/LuizYokoyama/java_ocr/blob/qrcode_readed_url/chromedriver_linux64/chromedriver?raw=true /home/chromedriver_linux64/chromedriver.exe

RUN chmod o+x /home/chromedriver_linux64/chromedriver.exe

# Open the port
EXPOSE 8080

# Copy our JAR
COPY target/$APP_FILE /app.jar

# Launch the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar" ]