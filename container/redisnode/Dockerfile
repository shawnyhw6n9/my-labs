FROM redis
EXPOSE 6379 16379
COPY ./container/redisnode/redis.conf /etc/redis/redis.conf
ENTRYPOINT redis-server /etc/redis/redis.conf