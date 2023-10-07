docker rmi identity-service:latest
docker build -t identity-service:latest .
docker tag identity-service:latest vebstechbee03/tech-bee:identity-service-latest
docker push vebstechbee03/tech-bee:identity-service-latest