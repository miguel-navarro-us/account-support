The application provides the option to perform operations through the command line. 
Use the corresponding number to select a choice, type the requested information and type Enter.

For example, given the above menu
    1 - Create new Account
    2 - Open existing Account
    3 - Exit
	
Type 2 if want to perform operations on an existing account. Then you will be requested to type the account number and PIN

The public services to process debits and checks are on:
{host:port}/account-management/public/checks
{host:port}/account-management/public/debits

Following x-www-form-urlencoded parameters should be included:
accountNumber
pin
description
amount

Response will be provided including operation result, description and transaction Id if applicable
	
DB creation script is included within this repository
	
To start the application:
./gradlew clean bootRun 

To run the tests:
./gradlew test

Java version:
1.8

Gradle version
Gradle 4.10