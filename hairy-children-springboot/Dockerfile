FROM java:8
EXPOSE 9001

VOLUME /tmp
ADD hairy-children-springboot.jar  /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]