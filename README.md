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