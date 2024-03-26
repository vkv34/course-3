#!/bin/bash

touch local.properties
echo "ftp.server=\"$1\"" >> local.properties
echo "ftp.username=\"$2\"" >> local.properties
echo "ftp.password=$3" >> local.properties