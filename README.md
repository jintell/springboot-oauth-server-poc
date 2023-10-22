# Getting Started

## Springboot Authorization Server POC Overview
This is a spring security proof of concept using spring boot 3.1.3 as the time of writing.

It follows the microservices design paradigm, although the projects are created in modules under a 
parent project [springboot-oauth-server-poc]()

The modules are:
1. [springboot-oauth-server](springboot-oauth-server): This module contains the custom Oauth server implementation using spring authorization server dependency
2. [springboot-api-resource-server](springboot-api-resource-server): This module contains the implementation of the protected exposed resource
3. [springboot-commandline-app](springboot-commandline-app): This module contains the implementation of the resource console client, written in spring.
4. [frontend-client](frontend-client): This module contains the implementation of resource web client, written in ReactJs


### RHow To Use this POC

You must run each module to test out the project. Each module have the README explaining what they do and to run them
respectively.

