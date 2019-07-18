FROM redis:5.0.5-alpine3.10
EXPOSE 6379 16379
COPY ./container/redisnode/rediscluster.conf /etc/redis/rediscluster.conf
ENTRYPOINT redis-server /etc/redis/rediscluster.conf