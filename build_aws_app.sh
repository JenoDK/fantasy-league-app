#!/bin/bash

./gradlew clean build -Pvaadin.productionMode --info
rm -rf deployment
mkdir deployment
cp build/libs/wk-app-1.0.0.jar deployment
cp -R .platform deployment
cp -R .ebextensions deployment
cd deployment/
zip -r wk-app-1.0.0.zip .platform/ .ebextensions/ wk-app-1.0.0.jar
cd ..