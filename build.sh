#!/bin/sh

if [ $# -ne 2 ]; then
	echo 1>&2 Usage: ./build.sh branch release|none
	exit 0
fi

# checkout the latest code from trunk
#git clone git@github.com:botskonet/Oracle.git
#cd Oracle

# checkout the proper branch
git checkout $1

# get the git revision number
gitvers=`git describe`

cp src/main/resources/plugin.yml src/main/resources/plugin-new.yml
mv src/main/resources/plugin.yml /tmp/plugin-old.yml

name=""
if [ "$1" == "master" ]; then
	name=$gitvers
else
	name="$gitvers-$1"
fi

nameNoV=`echo $name | cut -c 2-`

# add in revision
sed -e "s/nightly/$nameNoV/g" src/main/resources/plugin-new.yml > src/main/resources/plugin.yml
rm -f src/main/resources/plugin-new.yml

# make the jar
# handled by maven
#jar cf Oracle-$name.jar README.md LICENSE items.yml languages plugin.yml -C bin .

# Build maven
mvn package

# remove the build yml
rm src/main/resources/plugin.yml

# replace the old one
mv /tmp/plugin-old.yml src/main/resources/plugin.yml

# correct jar name
mv target/Oracle-nightly.jar target/Oracle-$name.jar

# send file to amazon bucket
#s3cmd put --acl-public target/Oracle-$name.jar s3://botsko/Oracle/Oracle-$name.jar

# Create a new version file
echo $name > versions.txt

if [ "$2" == "release" ]; then

	# send file to amazon bucket
	s3cmd put --acl-public versions.txt s3://botsko/Oracle/versions.txt
	# generate docs
	# javadoc -d docs-$name -sourcepath src/main/java -subpackages me.botsko.Oracle
fi

# Remove the files
rm versions.txt


echo "BUILD COMPLETE"
