cj-geochat-oauth2 is a microservices architecture implementation based on the standard Spring Boot/Cloud framework, utilizing a custom optimized simplified OAuth2 protocol. It fully supports front-end and back-end separation, multi-application architecture, and authentication for various client types. The project includes key modules such as the authentication server, API gateway, internal and external applications, and middle-tier services, all backed by the GeoChat capability library. The authentication server supports multiple authentication methods, including password and SMS verification codes, while the gateway provides efficient routing, authentication, rate limiting, and load balancing functionalities. Internal and external applications are built with standardized interfaces for flexible expansion and support remote calls via Feign. The project supports efficient automated publishing and deployment using Docker and Kubernetes, greatly enhancing development efficiency and enabling rapid microservices architecture deployment.

# cj-geochat-oauth2

This is a standard implementation of a microservices-based application architecture with the following capabilities:
> A standard Spring Boot/Cloud example implements microservices application architecture through a customized, simplified OAuth2 authentication protocol. It naturally supports a front-end and back-end separation architecture and extends authentication for various client types.
> It supports mechanisms compatible with mobile platforms like WeChat Mini Programs, providing foundational infrastructure support for SaaS.

# Project Introduction

In a distributed microservices environment, there are several critical roles, such as authentication, gateways, business microservices, and middle-tier microservices. Deciding how to divide or decompose these roles and determining which functionalities belong to which roles cannot rely solely on guidelines. Instead, it should follow a set of standardized architectural interfaces. Similar to Spring, where you must implement your functionalities within its standard interfaces, this project provides a complete set of standardized interfaces for microservices applications. Additionally, the project can be combined with my open-source foundational libraries, configuration center, and registration center to quickly build your microservices cloud architecture.
> To facilitate development and deployment, all GeoChat projects support automated publishing and deployment using Docker, Docker Compose, and Kubernetes (K8s). You need to configure your own Nexus and Docker Desktop to proceed with development.

Table of Contents

	•	Authentication Server
	•	Gateway
	•	Internal Applications
	•	External Applications
	•	Middle-Tier Applications
	•	Foundational Capability Library

## 1、Authentication Server

> cj-geochat-oauth2-server

Provides basic authentication methods: password, sms_code, blockchain_nonce, response_code, refresh_token.
> By relying on the following capability libraries, you can quickly implement your own authentication server based on them.

```xml

<dependency>
    <groupId>cj.geochat</groupId>
    <artifactId>cj-geochat-ability-oauth-server</artifactId>
</dependency>
```

```xml

<dependency>
    <groupId>cj.geochat</groupId>
    <artifactId>cj-geochat-ability-oauth-server-redis</artifactId>
</dependency>
```

## 2、api gateway

> cj-geochat-oauth2-gateway

Provides functions such as routing, authentication, rate limiting, and load balancing.
> You can quickly implement your gateway based on the following capability libraries.

```xml

<dependency>
    <groupId>cj.geochat</groupId>
    <artifactId>cj-geochat-ability-oauth-gateway</artifactId>
</dependency>
```

## 3、Internal Applications (Standard for Gateway-Backed Applications)

> cj-geochat-test-iapp  
> Internal Application Terminology: iApp, which stands for “inside app,” refers to an application within the gateway that handles requests from the gateway. However, it differs from a middle-tier application (middle app) in that:

	iApp handles user context and resource permission management.

### Usage

Project Structure:
> The official recommendation is that an iApp application should include a subproject with the following components: framework, remote, and starter.

#### framework：

> It is mainly used for interface and type definitions, with the functionality implemented by the starter project. The advantage of this approach is that other applications can reference the framework of this project in their remote project,

	thereby easily accessing the API services and types provided by this project.

#### remote：

> By using Feign to access remote APIs, it can directly utilize the types of the target project by referencing its framework.

#### starter:

> It is the functional implementation package of the iApp. The required functionality is enabled by activating the corresponding capability libraries.
> 你可以在你的项目包路径中建一个config包，将需要的能力加进去。

Open the Internal Application (InsideApp) Capability：

```java
@EnableCjInsideApp
@Configuration
public class OpenInsideAppConfig {

}

```
Open API Capability:

```java

@EnableCjApi
@Configuration
public class OpenApiConfig {
}

```

Open Eureka Registration Center Capability:

```java

@EnableCjEureka
@Configuration
public class OpenEurekaConfig {
}
```

Open Remote Call (Feign) Capability:

```java

@EnableCjFeign
@EnableFeignClients(basePackages = "cj.geochat.test.iapp.remote")
@Configuration
public class OpenFeignConfig {
}

```

Open Spring Doc Capability:

```java

@EnableCjSwagger
@Configuration
public class OpenSwaggerConfig {


}
```

## 4、External Application Standard (Applications that do not require a gateway)
> cj-geochat-test-oapp  
> 
> Since external applications are directly facing the public network, they provide authentication capabilities just like the gateway, and also have access control capabilities. 
>    
> The standard name for external applications is: oApp, which stands for outside app. 
> It has the same three subprojects as iApp: framework, remote, and starter. 

Open External Application (OutsideApp) Capability:

```java
@EnableCjOutsideApp
@Configuration
public class OpenOutsideAppConfig {
    @Bean
    public RestTemplate restTemplate(){
        var restTemplate=new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }
}

```
If additional capabilities are needed, you can add them yourself.

## 5、Middle-Tier Service Standard
> cj-geochat-test-middle  
> 
> Middle-tier applications are not open to the public network and are enclosed within the gateway. Therefore, they do not have access to user context or > permissions and are typical Spring Boot microservices.
> The standard name is “middle,” and it is recommended to add this name after the project name.
>
> It also recommends having the three subprojects: framework, remote, and starter.
>
> The required capabilities can be referenced as needed, which is the same as how iApp and oApp reference capabilities.
## 6、Capability Library
> The above all use the GeoChat capability library. For more capabilities, please refer to the GeoChat capability library. The open-source address for 》> the capability library project is as follows:  
> [https://github.com/carocean/cj-geochat-ultimate](https://github.com/carocean/cj-geochat-ultimate)
> 
# The authentication server has added verification code issuance and authorization endpoints.
-	SMS Verification Code: An interface has been provided for easy integration with third-party platforms.
-	Email Verification Code: An interface has been provided for easy integration with third-party platforms.
-	Visitor Verification Code: For open apps, obtaining an access token is crucial. The system will issue a temporary user and assign the “guests” role to access resources and receive message pushes.
