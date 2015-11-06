# A Gradle plugin for sql execute

[![Build Status](https://travis-ci.org/benweizhu/gradle-plugin-database.svg?branch=master)](https://travis-ci.org/benweizhu/gradle-plugin-database)

When dealing with the integration test, in most cases, you may need to interact with database, task like running sql statement could be very useful.

What you see right now is a Gradle plugin which provides you a task to run SQL files in order.

You can make your integrationTest depends on createDB task, so you can redeploy the database every time before integrationTest running. This gives you a chance to have a clean database environment.

###How to use
###Version 0.0.1

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'me.zeph:gradle-database-plugin:0.0.1'
    }
}

apply plugin: 'me.zeph.database'

configurations {
    database
}

dependencies {
    database 'mysql:mysql-connector-java:5.1.18'
}

database {
    url = 'jdbc:mysql://localhost:3306/your_database'
    username = 'root'
    password = ''
    driver = 'com.mysql.jdbc.Driver'
    configurationName = 'database'
    sqlFiles = files('config/database/my_sql.sql')
}
```

###Version 0.0.2 released
Version 0.0.2 supports 'drop and create database your_database_name' on demand before execute sql file 

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'me.zeph:gradle-database-plugin:0.0.2'
    }
}

apply plugin: 'me.zeph.database'

configurations {
    database
}

dependencies {
    database 'mysql:mysql-connector-java:5.1.18'
}

database {
    url = 'jdbc:mysql://localhost:3306/'
    username = 'root'
    password = ''
    databaseName = 'your_database'
    driver = 'com.mysql.jdbc.Driver'
    configurationName = 'database'
    sqlFiles = files('config/database/my_sql.sql')
}
```

###Sql file

my_sql.sql, use semicolon ';' to isolate each sql statement

```sql
DROP TABLE MY_TABLE;
CREATE TABLE MY_TABLE (
  ID   INT         NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(20) NOT NULL,
  CONSTRAINT MY_TABLE_PK PRIMARY KEY (ID)
);
```


```groovy
gradle createDB
```