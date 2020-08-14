# Asset Uploader Service
 
S3 Proxy Challenge :)

# Requirements
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Java 1.8](https://java.com/en/download/manual.jsp)
* [Docker](https://www.docker.com/)

## Getting Started

Start external service layers 

* Localstack S3
* Apache Cassandra
 
`docker-compose up` 

## Build
`./mvnw package`

## Start

`java -jar target/s3-proxy-challenge-0.0.1-SNAPSHOT.jar`

## Swagger UI

`http://localhost:8080/swagger-ui.html`

## Production Ready ? Not really :)

Few talking points:

* Security and Fraud Detection: the URL can be used multiple times until it expires, and if a malevolent hacker gets hands into that URL, your bucket will contain some unwanted data.
* Hot Partitioning Problem: It could be mitigated by adding to the partitioning key an extra date field 
* Cassandra Insert with TTL could mitigate storage size growth
* Observability: Monitoring and Alerting
* Stress Test
* Instance Sizing and Performance tuning
* Cost Estimation
* Add More Tests For Edge Cases and Cover S3 Service failures
* Authorization and Authentication
* Evaluate if a [Serverless architecture](https://medium.com/@laardee/uploading-objects-to-s3-using-one-time-presigned-urls-4374943f0801) could save money and remove the need to use the PUT endpoint
 

### Exercise Description
The goal of this exercise is to create a service that allows a user to upload an asset and then
request a time expiring URL to retrieve that asset.
Constraints:
● Any choice of language/stack is allowed
● Code should have adequate test coverage
● Code should be submitted with adequate instructions on how to set up and execute
● Due to past abuse, we cannot give you access to any of our Cloud accounts, please use
a free personal AWS account for the S3 component.
Functionally the API should have is described below:
1. Your service should have an HTTP POST endpoint that returns a signed S3 URL to
upload the asset to and a unique identifier for the asset
Requirements:
i. POST endpoint
ii. Request
1. Any required data
iii. Response
1. URL for upload
2. Unique identifier for the asset

2. The user should be able to make a POST call to the signed S3 URL from the response
in Step 1 to upload the asset.

3. To mark the upload operation complete, your service should provide a PUT endpoint as
follows:
Requirements:
i. PUT endpoint
ii. Request
1. Status flag
iii. Response
1. Indication of processing complete

4. When a GET request is made to your service for a particular asset id, your service
should return a signed S3 URL for download with the expiry timeout in seconds as a
URL parameter. Note: If the timeout is not specified assume 60 seconds.
Requirements:
i. GET endpoint
ii. Request
1. Timeout
iii. Response
1. URL for download

5. A GET call made on an asset that has not been set to “status: uploaded” from Step 3
should return an error to the user
6. The uploaded asset should be able to be fetched successfully using the signed URL in
the response from Step 4
Extra Credit: Is this service production ready? Why or why not? What would you need to
make it production ready?
