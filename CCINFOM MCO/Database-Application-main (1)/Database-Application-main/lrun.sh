#!/bin/bash
# Create the output directory if it doesn't exist
mkdir -p src/classes

# Path to connector
JAR_PATH="lib/mysql-connector-j-9.4.0/mysql-connector-j-9.4.0.jar"

# Compiling
javac -Xmaxerrs 10000 -Xmaxwarns 500 -Xlint \
	-cp "$JAR_PATH:src/classes" \
	-d src/classes \
	src/*.java src/model/*.java src/view/*.java src/controller/*.java


# Run main if compiling is successful 
if [ $? -eq 0 ]; then
   java -cp "$JAR_PATH:src/classes" Main 
fi

