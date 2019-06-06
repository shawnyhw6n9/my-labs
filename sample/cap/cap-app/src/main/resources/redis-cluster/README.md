1. Start redis cluster by config here (redis-cluster/*.conf)  
`redis-server redis-7000.conf & redis-server redis-7100.conf & redis-server redis-7200.conf & redis-server redis-7001.conf & redis-server redis-7101.conf & redis-server redis-7201.conf &`  
2. Add Slot to cluster  
`redis-cli -h 127.0.0.1 -p 7100 cluster addslots {0..5461}`  
`redis-cli -h 127.0.0.1 -p 7000 cluster addslots {5462..10922}`  
`redis-cli -h 127.0.0.1 -p 7000 cluster addslots {10923..16383}`  
3. Cluster handshake  
`redis-cli -p 7000`  
`cluster meet 127.0.0.1 7100`  
`cluster meet 127.0.0.1 7200`  
`cluster meet 127.0.0.1 7001`  
`cluster meet 127.0.0.1 7101`  
`cluster meet 127.0.0.1 7201`  
4. Replicate master as slave  
`CLUSTER REPLICATE XXXX`  
5. View Cluster info  
`cluster info`  
`cluster slots`  
6. Set key-value  
`set key1 value1`  
`get key1`  
`keys "*"`  