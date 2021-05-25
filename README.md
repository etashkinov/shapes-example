# Shapes example
An example service which stores different shapes and ensures their names are unique, and they are not intersected,
providing a RESTfull API

A shape consists of a Type, a Name and a Geometry Descriptor.
For the sake of simplicity, the only fully supported shape is an axis-aligned rectangle, not rotated.
Circle and Polygon are present, but not fully implemented.

## Building
The application uses min **Java 15**. So to build it, a valid path to JDK should be defined in a `JAVA_HOME` variable.

If you have maven installed, simple `mvn clean package` will do.

If not, run the maven wrapper as `mvnw clean package`.

The output is a ready-to-deploy Spring Boot application, located in `/target/shapes-example.jar`

## Running
You can run the app using `mvn spring-boot:run` command
or build a Docker image using the `Dockerfile` in the root directory

The app will start listening for requests under the default 8080 port

## Usage
The easiest way to try the app is to open `{app_host}/swagger-ui/#/shape-controller`

## Solution

The application can work properly with rectangles only, but is ready to be extended to other geometries,
thanks to a 2-step intersection checking approach.

On the first step a fast, indexed DB search against boundaries is performed, 
which cuts out the majority of shapes given the sparse data.

The next step is a slower in-app fine-tuned intersection detection, specific per each geometry type.

All coordinates are integers just for simplicity, any number type could be used depending on the requirements

## Architecture design

 1. For simplicity, in memory HSQL DB is used, so no  external DB needed to run the application. 
    The same HSQL is used for tests, so no additional test properties required
 1. The app consists of 3 main packages
    1. *.api.* - is the main part of the app, which defines its API. 
       Only DTO/POJOs and service interfaces are allowed there, no logic at all. 
       Should use no, or almost no external dependencies.
    1. *.impl.* - the implementation of the interface defined in *.api.* resides here. 
       That's the only place where business logic is allowed, including DB access and third-party integrations
    1. *.web.*, *.events.*, etc - packages which implement bridges between *.api.* and external clients, 
       such as REST APIs in *.web.*, or messaging consumers in *.events.*. 
       Should contain only the thin layer of logic specific to a particular bridge technology, 
       mostly adapting input data into *.api.* entities. Can access only *.api.* package, 
       and never touch anything inside *.impl.*
       
This is not the only way to structure your service by any means. There are many ways to design a microservice,
but such an approach worked much better for our small modular monolith modules or microservices,
in comparison with the others, like traditional layered, or hexagonal design.

## Disclaimer

The solution has quite a few limitations:
 1. No security aspect has been touched at all
 1. Logging and monitoring, being a required production application instrumentalization, are not present
 1. Some functionality is covered just a bit as a marker for the future evolution
 1. Swagger documentation is not fully configured