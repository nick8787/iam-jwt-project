FROM openjdk:11-jre

COPY target/*.jar /srv
COPY docker/iam-service.sh /srv

RUN chmod +x /srv/iam-service.sh

EXPOSE 8189

VOLUME [ "/srv/logs" ]

WORKDIR /srv

ENTRYPOINT ["./iam-service.sh"]
