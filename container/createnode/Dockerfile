FROM redis:5.0.5-alpine3.10
ENTRYPOINT echo "yes"|redis-cli --cluster create 172.19.0.2:6379 172.19.0.3:6379 172.19.0.4:6379 172.19.0.5:6379 172.19.0.6:6379 172.19.0.7:6379 --cluster-replicas 1