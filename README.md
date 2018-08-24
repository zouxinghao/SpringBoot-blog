# SpringBoot-blog
A blogging system developed by springboot / mybatis / thymeleaf


# Development log
## 08/23/2018
__Design and create database with mySQL, add docker support__
**Note**
1. mySQL docker with version higher than 5.7 may cause crash. 
   Reference: https://github.com/docker-library/mysql/issues/69
2. Using another .sql file can set the user and password for the database
   reference: https://github.com/zouxinghao/SpringBoot-blog/blob/master/docker-support/mysql/password.sql
## 08/24/2018
__Add User function, including create/modify users, log fuction__
**Note**
1. SLF4J vs Log4J:
SLF4J allows to use any independent specific log lib, besides that, SLF4J has better string mechanism like 
```java
logger.debug("Processing trade with id: {} and symbol : {} ", id, symbol);
```

