wfhome=`pwd`
cd $wfhome/..
wfhome=`pwd`

echo "$wfhome"

mvn -DskipTests=true -DskipGpg=false release:prepare
mvn -DskipTests=true -DskipGpg=false release:perform
