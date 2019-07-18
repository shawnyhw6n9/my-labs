# container
## docker-compose
1. Build image when first time.  
`docker-compose -f container/docker-compose.yml --project-directory container/ build`  
`docker images`  
2. Start up config server and application.  
`docker-compose -f container/docker-compose.yml --project-directory container/ up -d`  
3. Check container.  
`docker-compose -f container/docker-compose.yml --project-directory container/ ps`  
`docker logs container_app_1`  
`docker logs container_cloudconfig-1_1`  
`docker logs container_cloudconfig-2_1`  
4. Remove image.  
`docker rmi shawnyhw6n9/cap-app:${TAG}-dev shawnyhw6n9/cap-cloudconfig:${TAG}-dev`  
`docker images`  