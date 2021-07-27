# REST API tests

## Swagger (REST API for simple access to geo data)

https://xyz.api.here.com/hub/static/swagger/index.html#/

The following endpoints are tested:

- GET '/spaces'
- PUT/DELETE '/spaces/{spaceId}/features/{featureId}'

*Note:* the main test cases are implemented; the main checks are performed. There is always a room to imporve.

## How to run tests

1. Run tests manually (right click, run test)
2. Run maven command: ```mvn verify```

## How to get allure reports

If tests are executed with help of maven, then allure report is created. You can find it
under ```${project.basedir}/target/site/allure-maven-plugin/index.html```

## Link to CI on GitHub

https://github.com/bodaganj/BitpandaRestApiTesting/actions/workflows/mavenFinal.yml

## Tech stack

* RestAssured
* JUnit 5
* Allure
* Java 11
    * lombok
    * assertj/hamcrest
    * typesafe
* maven
* git/github

