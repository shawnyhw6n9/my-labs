# archetype
## Create Archetype

1. Create Archetype template from exist project  
* `mvn -f sample/cap/pom.xml archetype:create-from-project`  
* `mvn -f sample/cap/target/generated-sources/archetype clean install`  
* `export project_version=5.0.1`  
* `mkdir -p archetype/${project_version}`  
* `cp sample/cap/target/generated-sources/archetype/target/cap-archetype-${project_version}.jar archetype/${project_version}/cap5-archetype-${project_version}.jar && cp sample/cap/target/generated-sources/archetype/pom.xml archetype/${project_version}/cap5-archetype-${project_version}.pom`  

## Generate Project by local archetype

1. Install cap app version ${project_version} archetype source.  
* `export project_version=5.0.1`  
* `mvn install:install-file -DpomFile=archetype/${project_version}/cap5-archetype-${project_version}.pom -Dfile=archetype/${project_version}/cap5-archetype-${project_version}.jar`
2. Update maven local catalog.  
* `cp archetype/${project_version}/cap5-archetype-${project_version}.pom pom.xml`  
* `mvn archetype:update-local-catalog`  
* `rm pom.xml`  
3. Generate Project by local catalog.  
* `mvn archetype:generate -DarchetypeCatalog=local`  