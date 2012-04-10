wfhome=`pwd`
cd $wfhome/..
wfhome=`pwd`

echo "$wfhome"

mvn release:prepare
mvn release:perform
