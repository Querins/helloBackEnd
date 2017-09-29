[![Build Status](https://travis-ci.org/Querins/helloBackEnd.svg?branch=master)](https://travis-ci.org/Querins/helloBackEnd/builds/281231677)

To deploy this application you need to:
1. Create database "contactsdb" on MySQLServer
2. In file src/main/resources/application.properties change the value of properties spring.datasource.username and spring.datasource.password to
your username and password in MySQL.
3. Run command mvn spring-boot:run -Drun.profiles=default in project folder