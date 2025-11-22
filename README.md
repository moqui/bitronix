# Bitronix Transaction Manager (BTM)

Bitronix Transaction Manager (BTM) is a simple but complete implementation of
the Java Transaction API (JTA) 1.1. This eXtended Architecture (XA) transaction
manager provides all services required by the JTA while keeping the code as
simple as possible for easier understanding of XA semantics.

## Building

To build this project, you must have JDK17+ installed on your machine.

`./mvnw clean install`

## Javadocs

`./mvnw javadocs:javadocs`

Find the html at `btm/target/reports/apidocs/index.html` as well
as the javadocs jar

## Linting

check linting issues:
`./mvnw spotless:check`

fix linting issues:
`./mvnw spotless:apply`

## Original Authors

BTM was created and maintained by the Bitronix team, namely:

- [Ludovic Orban](https://x.com/bitronix)
- [Brett Wooldridge](https://x.com/BrettWooldridge). 
