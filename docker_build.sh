#!/bin/bash

docker build --no-cache --platform linux/amd64 --tag=pdfapp:latest .

docker run -p 8080:8080 --name pdfapp pdfapp:latest