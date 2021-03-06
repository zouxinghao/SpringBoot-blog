# SpringBoot-blog
A blogging system developed by springboot / mybatis / thymeleaf

[![Build Status](https://travis-ci.com/zouxinghao/SpringBoot-blog.svg?branch=master)](https://travis-ci.com/zouxinghao/SpringBoot-blog)

My blog with this project: http://xzou.me/

# Usage
## Quick Start:

*update*: you can also use the `runningTools` as `runningTools.sh start` to start the blog service. 

1. make sure you install mysql, jave and maven on your computers.
2. set up mysql service, you can also use docker with the code in "docker-support"
3. use `mvn build ...` and `java -jar ...` command to start the blog project


# TODO 
- [X] Currently, I used map for cache, and set size of 800, it is unsafe. Replacing it by redis may help.
- [ ] New: add "notebook" function, add more feasible PDF/Micrsoft docs reader, link [project](https://github.com/zouxinghao/Web-Online-Preview-File)
- [ ] Currently, I used cookie. I want to add Oauth for more functions.
- [X] slove the confliction of logback with JUnit 5.

# Display
## Article Page
![image](https://github.com/zouxinghao/SpringBoot-blog/raw/master/img/page.png)
## Backup
![image](https://github.com/zouxinghao/SpringBoot-blog/raw/master/img/backup.png)
There are manually backup bottom for databases and web files backup. 
Besides, there are crontab script running on my server, it will backup the database docker once a day. Check [here](https://github.com/zouxinghao/daily-toolset/blob/master/Docker/mysql/backup/backup.sh)

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

## 08/29/2018
__1. add WebMvcConfig.java, which is a extend class of WebMvcConfigurerAdapter, and will allow you to add customize function. And the addResourceHandlers function can help load static resource.__

__2. (pending) Try to add backup function, find that it is too complicated, so I will skip it for now. (update: finish this function in 09/11/2018)__ 

__3. update site function__

## 9/6/2018
__1. Transfer from Jenkins to Travis CI due to the limit of RAM. Integrate the test code__

## 9/14/2018
__1. add self-deployment with support of Travis CI__

## 9/19/2018
__add docker method to package and start-up the project__
