# gradle-plugin-database

###how to use

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