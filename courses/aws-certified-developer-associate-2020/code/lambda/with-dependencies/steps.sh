#!/bin/bash

# You need to have nodejs / npm installed beforehand
npm install aws-xray-sdk

# Set proper permissions for project files
chmod a+r * 

# You need to have the zip command available
zip -r function.zip .