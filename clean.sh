#!/bin/bash
cd src/main

rm *.class
rm *.tokens
rm Smoola*.java
rm *.interp
find . -type f -name "*.class" -delete