package me.zeph.gradle.database.task
import groovy.sql.Sql
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.sql.Driver
import java.sql.DriverManager

class CreateDatabaseTask extends DefaultTask {

    @TaskAction
    def createDatabase() {
        registerDriver(project.database.driver, project.database.configurationName)

        if (project.database.databaseName != null && project.database.databaseName.length() > 0) {
            def createDatabaseSqlInstance = getCreateDatabaseSqlInstance()
            executeCreateDatabaseSql(createDatabaseSqlInstance)
            createDatabaseSqlInstance.close()
        }

        def sqlExecuteSqlInstance = getSqlExecuteSqlInstance()
        executeSqlFile(sqlExecuteSqlInstance)
        sqlExecuteSqlInstance.close()
    }

    def executeCreateDatabaseSql(sqlInstance) {
        def dropDatabase = 'drop database ' + project.database.databaseName
        def createDatabase = 'create database ' + project.database.databaseName

        try {
            println dropDatabase
            sqlInstance.executeUpdate(dropDatabase)
            println "'${dropDatabase}' execute success"
        } catch (Exception e) {
            println "'${dropDatabase}' execute failure"
        }

        try {
            println createDatabase
            sqlInstance.executeUpdate(createDatabase)
            println "'${createDatabase}' execute success"
        } catch (Exception e) {
            println "'${createDatabase}' execute failure"
        }
    }

    def getCreateDatabaseSqlInstance() {
        getSqlInstance(project.database.url)
    }

    def getSqlExecuteSqlInstance() {
        getSqlInstance(getSqlExecuteUrl())
    }

    def getSqlInstance(url) {
        def sqlInstance = Sql.newInstance(url, project.database.username,
                project.database.password, project.database.driver)
        println sqlInstance.connection.catalog
        sqlInstance
    }

    def getSqlExecuteUrl() {
        if (project.database.url.endsWith('/')) {
            project.database.url + project.database.databaseName
        } else {
            project.database.url + '/' + project.database.databaseName
        }
    }

    def executeSqlFile(sql) {
        project.database.sqlFiles.each {
            File file ->
                executeSqlStatement(file, sql)
        }
    }

    def executeSqlStatement(File file, sqlInstance) {
        file.text.split(';').each {
            def sqlStatement = it.trim()
            if (!sqlStatement.isEmpty()) {
                try {
                    sqlInstance.execute(sqlStatement)
                    println "'${sqlStatement}' execute success"
                } catch (Exception e) {
                    println "'${sqlStatement}' execute failure"
                }
            }
        }
    }

    def registerDriver(driverName, configurationName) {
        URLClassLoader loader = GroovyObject.class.classLoader as URLClassLoader
        project.configurations[configurationName].each { File file -> loader.addURL(file.toURL()) }
        DriverManager.registerDriver(loader.loadClass(driverName).newInstance() as Driver)
    }
}
