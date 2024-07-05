**Important: Don't forget to update the [Candidate README](#candidate-readme) section**

# Real-time Transaction Challenge

## Overview

Welcome to Current's take-home technical assessment for backend engineers! We appreciate you taking the time to complete this, and we're excited to see what you come up with.

Today, you will be building a small but critical component of Current's core banking enging: real-time balance calculation through [event-sourcing](https://martinfowler.com/eaaDev/EventSourcing.html).

## Schema

The [included service.yml](service.yml) is the OpenAPI 3.0 schema to a service we would like you to create and host.

## Details

The service accepts two types of transactions:

1. Loads: Add money to a user (credit)

2. Authorizations: Conditionally remove money from a user (debit)

Every load or authorization PUT should return the updated balance following the transaction. Authorization declines should be saved, even if they do not impact balance calculation.

You may use any technologies to support the service. We do not expect you to use a persistent store (you can you in-memory object), but you can if you want. We should be able to bootstrap your project locally to test.

## Expectations

We are looking for attention in the following areas:

1. Do you accept all requests supported by the schema, in the format described?

2. Do your responses conform to the prescribed schema?

3. Does the authorizations endpoint work as documented in the schema?

4. Do you have unit and integrations test on the functionality?

# Candidate README

## Bootstrap instructions

Replace this: To run this server locally, do the following in the root directory:  
`mvn clean install`  
`mvn spring-boot:run`

## Design considerations

- I have used lombok framework to automatically generate repetitive 'get' and 'set' functions. It's a very widely used framework, but not something I was familiar with. So, I wanted to give it a shot!

- I took into consideration currency type mismatches. For example: if a authorization is made in CAD and the existing userId has USD balance, then it will be DECLINED and no change will be performed on the balance. Similarly, if such a load is performed, then there will be no change on the existing balance of the userId (since the schema doesn't have an APPROVED or DENIED for load).

- I made some tests for the enpoints to test their functionality. For load I check the expected response on the balance variables. Similarly, for authorization, I perform a load and then perform an authorization. For ping, I check the formatting to match date-time format. Had to use regex to get there.

- I wasn't sure what the instructions meant about "saving the authorization declines". But i assumed that a log was needed? I really tried to get log4j to create a log file but for some reason it just wouldn't find the config file (same with logback). So, at the very least it logs the declines on the console. In general, the decline responses work as per schema.

## Bonus: Deployment considerations

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

## License

At CodeScreen, we strongly value the integrity and privacy of our assessments. As a result, this repository is under exclusive copyright, which means you **do not** have permission to share your solution to this test publicly (i.e., inside a public GitHub/GitLab repo, on Reddit, etc.). <br>

## Submitting your solution

Please push your changes to the `main branch` of this repository. You can push one or more commits. <br>

Once you are finished with the task, please click the `Submit Solution` link on <a href="https://app.codescreen.com/candidate/c6007a5c-16c9-4f6c-9538-260926342829" target="_blank">this screen</a>.
