mvn clean install
docker build -t identity-service-0.0.1-RELEASE .
docker tag identity-service:0.0.1-RELEASE vebstechbee03/tech-bee:identity-service-0.0.1-RELEASE
docker push vebstechbee03/tech-bee:identity-service-0.0.1-RELEASE
