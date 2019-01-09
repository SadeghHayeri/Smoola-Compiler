#!/bin/bash
export CLASSPATH=".:/usr/local/lib/antlr-4.7.1-complete.jar:$CLASSPATH"
rm -rf *.j
cd ../src/main

java -jar /usr/local/lib/antlr-4.7.1-complete.jar Smoola.g4
javac *.java
java -ea org.antlr.v4.gui.TestRig Smoola program < ../../test/$1

cd ../../
./clean.sh

cd ./out
java -jar jasmin.jar *.j