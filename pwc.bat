@echo off
cd /d %~dp0
echo Starte Docker Compose...
docker-compose up -d

echo Starte Spring Boot Anwendung...
java -jar target\powerconsumption-0.0.1-SNAPSHOT.jar

echo Stoppe Docker Compose...
docker-compose down
pause
