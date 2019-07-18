FROM redis
EXPOSE 26379
COPY ./container/sentinel/sentinel.conf /etc/redis/sentinel.conf
ENTRYPOINT redis-server /etc/redis/sentinel.conf --sentinel