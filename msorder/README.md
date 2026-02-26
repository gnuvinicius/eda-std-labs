mvn -B archetype:generate \
  -DarchetypeGroupId=org.wildfly.archetype \
  -DarchetypeArtifactId=wildfly-jakartaee-webapp-archetype \
  -DarchetypeVersion=33.0.0.Final \
  -DgroupId=br.dev.garage474 \
  -DartifactId=msorder \
  -Dversion=1.0-SNAPSHOT \
  -Dpackage=br.dev.garage474.msorder