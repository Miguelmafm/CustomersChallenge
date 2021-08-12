# CustomersChallenge

## Challenge Description:

Write a microservice backed up by a database that exposes APIs to:

list Customers 
get Customer
create Customers 
Update Customer
Delete Customers (Only to be executed by superusers)
Use Spring-boot to implement your microservice. Make it as complete as possible and use mongo as your database taking the requirements below into consideration:

The customer should have a name, phone number, birth date, and credit limit
When we get the customer detail we also want its age to be returned in the API.
Add any performance enhancements you see fit.
Use the development best practices applicable.

## MongoDB configs(If needed change in project file "application.properties"):
```bash
localhost:27017
user: "admin"
password: "password"
DB: "customers"
```

## Api documentation:

**List Customers** -> GET "http://localhost:8080/api/v1/customers"<br />
**Get Customer** -> GET "http://localhost:8080/api/v1/customers/{id}"<br />

**Create Customers** -> POST "http://localhost:8080/api/v1/customers"<br />
Request body:
```java
{
    "name": "John Doe",  //required field
    "phoneNumber": "944745100",
    "birthDate": "1990-10-13",  // required field with format ("yyyy-MM-dd")
    "creditLimit": 1000
}
```

**Update Customer** -> PUT "http://localhost:8080/api/v1/customers"<br />
Request body:
```java
{
    "id": "61131d3d8392e34a497e34c2",
    "name": "John Doe",  //required field
    "phoneNumber": "944745100",
    "birthDate": "1990-10-13",  // required field with format ("yyyy-MM-dd")
    "creditLimit": 500
}
```
<br />


**Delete Customer** -> DELETE "http://localhost:8080/api/v1/customers/{id}"  (**Only to be executed by superusers "admin"**)<br />
Its required to pass an Header "Authorization" with a JWT token ex: 
```bash
"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyODY0Mjk2NiwiZXhwIjoxNjI4NzI5MzY2fQ"
```
<br />

## In order to Delete Customer its required 'admin' permissions. Documentation for Authentication api's Services:

**Signup New User** -> POST "http://localhost:8080/api/auth/signup"<br />
Request body:
```java

{
    "username": "admin",  //required field (cannot be an existing one)
    "email": "admin@teste.com",  //required field (min = 3, max = 20 chars) (cannot be an existing one)
    "password": "123456",   //required field (min = 6, max = 40)
    "roles": ["admin", "user"]    //"admin" or "user"
}
```
<br />
**Login User** -> POST "http://localhost:8080/api/auth/login" (use the generated JWT to invoke DELETE)<br />
```java

Request body:
{
    "username": "admin",  //required field
    "password": "123456"  //required field
}
```

