mvn -f sample/cap/cap-app/pom.xml archetype:create-from-project
mvn -f sample/cap/cap-app/target/generated-sources/archetype clean install
export project_version=5.0.1
mkdir -p archetype/${project_version}/cap-app/
cp sample/cap/cap-app/target/generated-sources/archetype/target/cap-app-archetype-${project_version}.jar archetype/${project_version}/cap-app/cap-app-archetype-${project_version}.jar && cp sample/cap/cap-app/target/generated-sources/archetype/pom.xml archetype/${project_version}/cap-app/cap-app-archetype-${project_version}.pom
export project_version=5.0.1
mvn install:install-file -DpomFile=archetype/${project_version}/cap-app/cap-app-archetype-${project_version}.pom -Dfile=archetype/${project_version}/cap-app/cap-app-archetype-${project_version}.jar
cp archetype/${project_version}/cap-app/cap-app-archetype-${project_version}.pom pom.xml
mvn archetype:update-local-catalog
rm pom.xml
mvn archetype:generate -DarchetypeCatalog=local
