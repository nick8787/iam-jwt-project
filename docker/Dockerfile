FROM openjdk:11-jre

# Копирование JAR файла из директории target проекта в /srv внутри образа
COPY target/security-jwt-0.0.1-SNAPSHOT.jar /srv/security-jwt-0.0.1-SNAPSHOT.jar

# Копирование скрипта из папки docker, так как Dockerfile тоже там
COPY docker/iam-service.sh /srv/iam-service.sh

RUN chmod +x /srv/iam-service.sh

EXPOSE 8189

VOLUME [ "/srv/logs" ]

WORKDIR /srv

ENTRYPOINT ["./iam-service.sh"]
