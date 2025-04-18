# About

This projects implements a schema for handling the transactions for a Virtual Wallet. The endpoints supported can Add Money to the user (Currency can be specified) or Charge/Remove money from the user. 

## Schema

The [included service.yml](service.yml) is the OpenAPI 3.0 schema.

## Details

The service accepts two types of transactions:

1. Loads: Add money to a user (credit)

2. Authorizations: Conditionally remove money from a user (debit)

Every load or authorization PUT returns the updated balance following the transaction. Authorization declines are saved, even if they do not impact balance calculation.

## Bootstrap instructions

To run this server locally, do the following in the root directory:  
`mvn clean install`  
`mvn spring-boot:run`

## Design considerations

- I have used lombok framework to automatically generate repetitive 'get' and 'set' functions. It's a very widely used framework, but not something I was familiar with. So, I wanted to give it a shot!

- I took into consideration currency type mismatches. For example: if a authorization is made in CAD and the existing userId has USD balance, then it will be DECLINED and no change will be performed on the balance. Similarly, if such a load is performed, then there will be no change on the existing balance of the userId (since the schema doesn't have an APPROVED or DENIED for load).

- I made some tests for the enpoints to test their functionality. For load I check the expected response on the balance variables. Similarly, for authorization, I perform a load and then perform an authorization. For ping, I check the formatting to match date-time format. Had to use regex to get there.

## Future work considerations for deployment

- I would firstly containerize the application using Docker. This gives it the flexibility to be deployed in any environment.

- Considering that this is a transaction handler, we need to think about the scaling and load balancing. So, a cloud platform like AWS would be ideal as it can automatically scale up or down based on traffic. It will also help with establishing multi-region deployments to expand in the future.

- Definitely need a database to store all the user data as well as transaction data. Also needs backups in place to have redundancy. This is also something that's better on cloud. However, we'll have to make sure the IAM roles and VPCs are properly configured to make the databases secure.

## ASCII art

```
                              $$\       $$\ $$\
                              $$ |      \__|$$ |
 $$$$$$$\ $$$$$$$\   $$$$$$\  $$$$$$$\  $$\ $$ |
$$  _____|$$  __$$\ $$  __$$\ $$  __$$\ $$ |$$ |
\$$$$$$\  $$ |  $$ |$$$$$$$$ |$$ |  $$ |$$ |$$ |
 \____$$\ $$ |  $$ |$$   ____|$$ |  $$ |$$ |$$ |
$$$$$$$  |$$ |  $$ |\$$$$$$$\ $$ |  $$ |$$ |$$ |
\_______/ \__|  \__| \_______|\__|  \__|\__|\__|



```

