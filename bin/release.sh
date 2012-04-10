cd ..

mvn clean

mvn -DskipTests=true -DskipGpg=false release:prepare

mvn -DskipTests=true -DskipGpg=false release:perform