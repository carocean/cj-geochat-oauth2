# cj-geochat-oauth2

这是一套微服务应用化架构的标准实现，它有以下能力：
> 标准springboot/cloud例程，通过自定义的简化版oauth2认证协议，实现微服务应用化，天然支持应用前后端分离架构，多种客户端类型认证扩展。
> 支持像微信这种移动平台兼容小程序的机制，为Saas提供基础架构支撑。

# 项目介绍

在分布式微服务环境中，有一些重要的角色，如认证、网关、业务微服务及中台服务微，如何划分，或如何拆解，哪些功能要放到哪些角色里去，不能单纯通过规范要求，
而应该是一套架构的标准接口，就像spring一样你必须在其接口标准下实现你的功能。因此本项目提供了一整套微服务应用化标准接口。另外以下项目可以结合本人开源的基础能力库、配置中心、注册中心直接搭建你的微服务云架构。
> 为了便于开发和部署，geochat的所有项目均支持docker,docker-compose,k8s的自动发布和部署，你需要配置你自己的nexus和docker-desktop，以便进行开发。

本文档目录
- 认证服务器
- 网关
- 内应用
- 外应用
- 中台应用
- 基础能力库

## 1、认证服务器

> cj-geochat-oauth2-server

提供基本的认证方式：password, sms_code, blockchain_nonce, response_code, refresh_token
> 依赖以下能力库，你可以根据这些能力库快速实现自己的认证服务器

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

## 2、api网关

> cj-geochat-oauth2-gateway

提供路由、鉴权、限流、负载等功能。
> 你可以根据以下能力库快带实现你的网关

```xml

<dependency>
    <groupId>cj.geochat</groupId>
    <artifactId>cj-geochat-ability-oauth-gateway</artifactId>
</dependency>
```

## 3、内应用（网关后置应用标准）

> cj-geochat-test-iapp  
> 内应用专有名词：iapp，即：inside app，表示该应用是网关内的应用，处理来自网关的请求。但中台应用（middle app)不同在于，
> iapp有用户上下文和资源权限处理。

### 用法

项目结构：
> 官主推荐一个iapp应用要包括一个子项目，分别是：framework、remote、starter

#### framework：

> 主要用于接口和型形定义，由starter项目来实现功能，这样做的好处是，其它的应用可通过在其remote项目中引用该项目的framework，
> 从而便捷的引用到该项目提供的api服务及类型。

#### remote：

> 通过feign访问远程api，它可借助于直接引用目标项目的framework而直接使用其类型。

#### starter:

> 是iapp的功能实现包，需要什么样的功能就开启什么能力库。
> 你可以在你的项目包路径中建一个config包，将需要的能力加进去。

开放内应用(InsideApp)能力：

```java
@EnableCjInsideApp
@Configuration
public class OpenInsideAppConfig {

}

```
开放Api能力：

```java

@EnableCjApi
@Configuration
public class OpenApiConfig {
}

```

开放Eureka注册中心能力：

```java

@EnableCjEureka
@Configuration
public class OpenEurekaConfig {
}
```

开放远程调用(feign)能力：

```java

@EnableCjFeign
@EnableFeignClients(basePackages = "cj.geochat.test.iapp.remote")
@Configuration
public class OpenFeignConfig {
}

```

开放spring doc能力：

```java

@EnableCjSwagger
@Configuration
public class OpenSwaggerConfig {


}
```

## 4、外应用标准（不需要网关的应用）
> cj-geochat-test-oapp  
> 
> 由于外应用是直接面向公网的应用，因此它和网关一样提供鉴权能力，同时还具有访问控制能力。  
>    
> 外应用标准名是：oapp，即outside app。  
> 它与iapp一样具有framework,remote,starter三类子项目。  

开放外应用(OutsideApp)能力：

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
如需要其它能力可自行添加。

## 5、中台服务标准
> cj-geochat-test-middle  
> 
> 中台应用不对外网开放，被封密在网关之内，因此没有权限和用户上下文，是springboot的一般微服务。  
> 标准名称是middle,建议在项目名称后面加上此名称。  
> 
> 它一样推荐有framework,remote,starter三个子项目。  
> 
> 需要什么能力就自行引用，这点根iapp和oapp引用能力的方式相同。
> 
## 6、能力库
> 以上均用到geochat能力库，更多能力，请到geochat能力库中查看，能力库项目的开源地址如下：  
> [https://github.com/carocean/cj-geochat-ultimate](https://github.com/carocean/cj-geochat-ultimate)