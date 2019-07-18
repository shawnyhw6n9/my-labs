FROM redis:5.0.5-alpine3.10
EXPOSE 26379
COPY ./container/sentinel/sentinel.conf /etc/redis/sentinel.conf
ENTRYPOINT redis-server /etc/redis/sentinel.conf --sentinel