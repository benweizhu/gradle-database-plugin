# A Gradle plugin for sql execute

When dealing with the integration test, in most cases, you may need to interact with database, task like running sql statement could be very useful.

What you see right now is a Gradle plugin which provide you a task to run SQL files in order.

You can make your integrationTest depends on createDB task, so you can redeploy the database everytime before integrationTest running. This gives you a chance to have a clean database environment.

###How to use

```groovy
buildscript {
    repositories {
        maven {
            url "http://dl.bintray.com/benweizhu/maven"
        }
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
    sqlFiles = files('config/database/drop_table.sql')
}
```

```groovy
gradle createDB
```