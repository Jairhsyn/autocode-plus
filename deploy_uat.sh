#!/usr/bin/env bash
cd front
cnpm i && npm run build
cd ../
mvn clean install -Dmaven.test.skip=true
cd target
cp ROOT.jar ROOT.new.jar
echo `shasum ROOT.new.jar | cut -d' ' -f1` > sha.txt
tar -zcvf ROOT.new.tar.gz ROOT.new.jar sha.txt
scp ROOT.new.tar.gz root@washmore.tech:/www/wwwroot/file
ssh -oPort=7020 root@washmore.tech "cd /home/apps/jyb/temp && rm -rf * && wget https://file.washmore.tech/ROOT.new.tar.gz && sh /home/apps/jyb/run.sh"