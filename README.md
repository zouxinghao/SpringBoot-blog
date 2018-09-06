# SpringBoot-blog
A blogging system developed by springboot / mybatis / thymeleaf
[![Build Status](https://travis-ci.com/zouxinghao/SpringBoot-blog.svg?branch=master)](https://travis-ci.com/zouxinghao/SpringBoot-blog)

# TODO 
- [ ] Currently, I used map for cache, and set size of 800, it is unsafe. Replacing it by redis may help.
- [ ] Currently, I used cookie. The other method like OAuth may be more funtional.
- [ ] slove the confliction of logback

# Development log
## 08/23/2018
__Design and create database with mySQL, add docker support__
**Note**
1. mySQL docker with version higher than 5.7 may cause crash. 
   Reference: https://github.com/docker-library/mysql/issues/69
2. Using another .sql file can set the user and password for the database
   reference: https://github.com/zouxinghao/SpringBoot-blog/blob/master/docker-support/mysql/password.sql
## 08/24/2018
__1.Add User function, including create/modify users, log fuction__

__2.Add Mapping(XML) / service / controller (RESTful) for User/admin function__

__3.Minor funtion: cache / Exception / message bar__

**Note**
1. SLF4J vs Log4J:
SLF4J allows to use any independent specific log lib, besides that, SLF4J has better string mechanism like 
```java
logger.debug("Processing trade with id: {} and symbol : {} ", id, symbol);
```

## 08/26/2018
__1. Add Unit Test (with JUnit)__

__2. Add front end code (htnl/css/js) (through thymleaf with templates)__

__3. Debug for docker function__

**Note**
1. ➖ remove JUnit to solve confliection (see development log): JUnit will use logback, while SLF4J will also use it, which cause the confliection, see [reference](https://github.com/spring-projects/spring-boot/issues/4341). I will revert it back once I figure out how to solve the confliction. 
2. AES/MD5 with salt

## 08/27/2018
__1. add UUID for user (update on 08/29/2018: also for attach)__

## 08/28/2018
__1. __

## 08/29/2018
__1. add WebMvcConfig.java, which is a extend class of WebMvcConfigurerAdapter, and will allow you to add customize function. And the addResourceHandlers function can help load static resource.__

__2. (pending) Try to add backup function, find that it is too complicated, so I will skip it for now.__ 

__3. update site function__

## 9/6/2018
__1. Transfer from Jenkins to Travis CI due to the limit of RAM. Integrate the test code

