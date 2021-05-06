#!/bin/bash

./gradlew clean build -Pvaadin.productionMode --info
rm -rf deployment
mkdir deployment
cp build/libs/wk-app-1.0.0.jar deployment
cp -R .platform deployment
cd deployment/
zip -r wk-app-1.0.0.zip .platform/ wk-app-1.0.0.jar
cd ..