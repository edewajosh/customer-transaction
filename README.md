## The application is using auth2.0 bearer token
* Create DB ON POSTGRES
```CREATE DATABASE BANKING```
* INSTALL ACTIVEMQ and run
## CREATE USER with this credentials
```
password = "janedoe5@test.com";
username =  "Password@123";
```

### AUTH LOGIN
```curl --location 'http://localhost:8080/api/v1/auth/login' \
--header 'Authorization: Basic am9obmRvZUB0ZXN0LmNvbTp0ZXN0QDEyMzQ1Ng==' \
--header 'Cookie: JSESSIONID=5C768B596AE967BFA80CD55109CA5A5C'
```
### CREATE CUSTOMER
```
curl --location 'localhost:8080/api/v1/auth/register' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=4D67F29E5CBD23E67FE9CD7B33481E0D' \
--data-raw '{
    "firstName": "Jane",
    "middleName": "Test",
    "lastName": "Doe",
    "email": "janedoe@test.com",
    "password": "Password@123",
    "active": true,
    "accountNumber": "987654443",
    "accountType": "SALARY",
    "balance": 100000,
    "accountStatus": "OPEN",
    "currencyCode": "KES"
}'
```
### CASH DEPOSIT
```
curl --location 'localhost:8080/api/v1/transactions/deposit' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=5C768B596AE967BFA80CD55109CA5A5C; JSESSIONID=4D67F29E5CBD23E67FE9CD7B33481E0D' \
--data '{
    "amount": 12000.0,
    "accountID": "1",
    "description": "HGDFFSD GGFDS",
    "transactionType": "ACU",
    "creditAccount": "123456789",
    "currencyCode": "KES"
}'
```