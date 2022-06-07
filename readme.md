# Getting Started

### Prerequisites
    Java 18
    Maven

### Running tests
#### Run all tests
    mvn clean test

#### Creating a new configuration file
Simply copy the src\test\resources\application.yml into a new file (e.g. application-preprod.yml) and overwrite any configurations you would like to be specific for that profile

#### Run tests against specific configuration file
Once you have a new config file setup, you can run the tests with that specific configuration by runnign the following name (e.g. profile-name == preprod)
 
    mvn test  -D"spring.profiles.active"=${profile-name}
