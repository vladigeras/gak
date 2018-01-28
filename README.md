# Description
System for examination commission

# Run as Development profile
If you need run project with HOT LOAD classes automatically, then run 
#### 1. frontend module as:
  ```sh
  $ cd frontend/src/main/frontend
  $ npm start
  ```
#### 2. backend module as:
  ```sh
  $ cd backend
  $ mvn spring-boot:run
  ```
### Note: after upgrading to java 9 spring-boot devtools, which provide a auto update Java code, not working. I hope, next upgrades Spring-Boot repair it !!! 
  
# Run as Production
#### 1. From root module:
  ```
  $ mvn clean install
  ```
#### 2. From backend module:
  ```
  $ cd backend
  $ mvn spring-boot:run
  ```