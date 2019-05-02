1. Start eureka server by docker.  
`docker run --name eureka -h eureka -d -p 8761:8761 springcloud/eureka`
2. Start config server by Spring Boot.  
`java -jar -Dserver.port=8001 target/cap-cloudconfig-5.0.0.jar`
3. Start backup config server.  
`java -jar -Dserver.port=8003 target/cap-cloudconfig-5.0.0.jar`