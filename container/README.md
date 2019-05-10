# container
## docker-compose
1. Build image when first time.  
`docker-compose -f container/docker-compose.yml build`  
`docker images`  
2. Start up config server and application.  
`docker-compose -f container/docker-compose.yml up -d`  
3. Check container.  
`docker-compose -f container/docker-compose.yml ps`  
`docker logs container_app_1`  
`docker logs container_cloudconfig_1_1`  
`docker logs container_cloudconfig_2_1`  
4. Remove image.  
`docker rmi shawnyhw6n9/cap-app:5.0.0-dev shawnyhw6n9/cap-cloudconfig:5.0.0-dev`  
`docker images`  