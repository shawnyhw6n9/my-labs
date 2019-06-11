# archetype
## Generate Project by local archetype

1. Install cap app version ${project.version} archetype source.  
`mvn install:install-file -DpomFile=archetype/${project.version}/cap5-archetype-${project.version}.pom -Dfile=archetype/${project.version}/cap5-archetype-${project.version}.jar`
2. Update maven local catalog.  
`cp archetype/${project.version}/cap5-archetype-${project.version}.pom pom.xml`  
`mvn archetype:update-local-catalog`  
`rm pom.xml`  
3. Generate Project by local catalog.  
`mvn archetype:generate -DarchetypeCatalog=local`
