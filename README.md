[![Build Status](https://travis-ci.org/Querins/helloBackEnd.svg?branch=master)](https://travis-ci.org/Querins/helloBackEnd/builds/281231677)

To deploy this application you need to:
1. Create database "contactsdb" on MySQLServer
2. In file <b>src/main/resources/application.properties</b> change the value of properties <b>spring.datasource.username</b> and <b>spring.datasource.password</b> to your username and password in MySQL.
3. Run command <b>mvn spring-boot:run -Drun.profiles=default</b> in project folder
