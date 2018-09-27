# Description
System for examination commission

## Installation:
1. Install **Java 10**.
2. Install **PostgreSQL 10** and bind to port **5432**

    - Create user in Postgres:
        ```sh
        CREATE USER gak WITH PASSWORD 'gak';
        ```
    
    - Create database for new user:
        ```sh
        CREATE DATABASE gak WITH OWNER gak ENCODING 'UTF8';
        ```
    
    - Tables will be generated
   
3. Execute a command for **root** module:
    ```sh
    $ mvn clean install
    ``` 

## Start project
1. Execute a command for **backend** module to start as Maven project:
    ```sh
    $ mvn spring-boot:run
    ```
2. Application will be available on [http://localhost:8080](http://localhost:8080)
3. In this case you must rebuild project after **frontend** changing to show results, 
    ```sh
    $ mvn clean install 
    ```

### Run frontend separately with code autoupdate
1. Execute a command for **backend** module to start backend part:
   ```sh
   $ mvn spring-boot:run
   ```
2. Execute a command for **frontend** module to start frontend part:
    ```sh
   $ npm start
   ```
3. Application will be available on [http://localhost:8080](http://localhost:8080)
4. Frontend part will be available on [http://localhost:4200](http://localhost:4200)
5. In this case after frontend changing result will be showed.

Read this [title](https://youtrack.jetbrains.com/issue/IDEA-141638) to code autoupdate in backend part. 
Library **spring-boot-devtools** as a maven dependency will help. 