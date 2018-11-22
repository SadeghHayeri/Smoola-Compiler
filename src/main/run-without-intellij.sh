#!/bin/bash
./clean.sh

java -jar /usr/local/lib/antlr-4.7.1-complete.jar Smoola.g4
javac *.java
java org.antlr.v4.gui.TestRig Smoola program < ../../test/$1
