# archetype
## Generate Project by local archetype

1. Install cap app v5.0.0 archetype source.  
`mvn install:install-file -DpomFile=archetype/5.0.0/cap5-archetype-5.0.0.pom -Dfile=archetype/5.0.0/cap5-archetype-5.0.0.jar`
2. Update maven local catalog.  
`cp archetype/5.0.0/cap5-archetype-5.0.0.pom pom.xml`  
`mvn archetype:update-local-catalog`  
`rm pom.xml`  
3. Generate Project by local catalog.  
`mvn archetype:generate -DarchetypeCatalog=local`
