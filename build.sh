mvn -f sample/cap/pom.xml clean package
docker-compose -f container/docker-compose.yml --project-directory container/ up --build