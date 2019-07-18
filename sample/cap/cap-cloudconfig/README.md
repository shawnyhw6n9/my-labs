1. Start eureka server by docker. (Or start by @EnableEurekaServer)  
`docker run --name eureka -h eureka -d -p 8761:8761 springcloud/eureka`
2. Start config server by Spring Boot.  
`java -jar -Dserver.port=8001 target/cap-cloudconfig-${project.version}.jar`
3. Start backup config server.  
`java -jar -Dserver.port=8003 target/cap-cloudconfig-${project.version}.jar`
4. The HTTP service has resources in the following form:
`/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties`