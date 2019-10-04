1. Start redis.  
`redis-server&`  
2. Start jconsole.  
`jconsole&`  
3. Build cap-app.  
`mvn -f /Users/shawnyhw6n9/Documents/study/study_microservices_poc/my-labs/sample/pom.xml clean package`  
4. Start cap-app on 8080.  
`cd /Users/shawnyhw6n9/Documents/study/study_microservices_poc/my-labs/sample/cap/cap-app/`  
`java -Xmx512m -Xss512k -Dspring.profiles.active=dev -Dserver.port=8080 -Dspring.redis.isEmbedServer=false -jar target/cap-app-5.0.1.jar`  
5. Start cap-app on 8081.  
`java -Xmx512m -Xss512k -Dspring.profiles.active=dev -Dserver.port=8081 -Dspring.redis.isEmbedServer=false -jar target/cap-app-5.0.1.jar`   
6. Check java process.
`top | grep java`  
7. Start jmeter.  
Test only on 8080 java.
`cd /Users/shawnyhw6n9/Documents/study/study_microservices_poc/my-labs/jmeter/`  
`jmeter -n -t template.jmx`  
`top -pid $(pgrep -f 'java -Xmx512m -Xss512k')`  
8. ls log files.  