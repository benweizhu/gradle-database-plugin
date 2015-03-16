# gradle-plugin-database

###how to use

apply plugin: 'me.zeph.database'

database {
    url = 'jdbc:mysql://localhost:3306/your_database'
    username = 'root'
    password = ''
    driver = 'com.mysql.jdbc.Driver'
    configurationName = 'database'
    sqlFiles = files('config/database/drop_table.sql')
}