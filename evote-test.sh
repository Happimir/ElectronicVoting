#!/bin/sh

export CLASSPATH=./lib/mysql-connector-java-5.1.33-bin.jar

javac -d classes -cp src/edu/uga/cs/evote/EVException.java src/edu/uga/cs/evote/entity/*.java src/edu/uga/cs/evote/entity/impl/*.java src/edu/uga/cs/evote/object/*.java src/edu/uga/cs/evote/object/impl/*.java src/edu/uga/cs/evote/persistence/*.java src/edu/uga/cs/evote/persistence/impl/*.java src/edu/uga/cs/evote/test/*.java 

java -cp classes:/opt/classes/mysql-connector-java-5.1.37-bin.jar src.edu.uga.cs.evote.test.EvoteTester.java
