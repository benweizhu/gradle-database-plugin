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
            def createDatabaseSqlInstance = getSqlInstance(project.database.url)
            executeCreateDatabaseSql(createDatabaseSqlInstance)
            createDatabaseSqlInstance.close()
        }

        def sqlExecuteSqlInstance = getSqlInstance(getSqlExecuteUrl())
        executeSqlFile(sqlExecuteSqlInstance)
        sqlExecuteSqlInstance.close()
    }

    def executeCreateDatabaseSql(sqlInstance) {
        executeSql('drop database ' + project.database.databaseName, sqlInstance)
        executeSql('create database ' + project.database.databaseName, sqlInstance)
    }

    def executeSql(sql, sqlInstance) {
        try {
            println "start '${sql}'"
            sqlInstance.executeUpdate(sql)
            println "'${sql}' execute success"
        } catch (Exception e) {
            println "'${sql}' execute failure"
        }
    }

    def getSqlInstance(url) {
        def sqlInstance = Sql.newInstance(url, project.database.username,
                project.database.password, project.database.driver)
        println sqlInstance.connection.catalog
        sqlInstance
    }

    def getSqlExecuteUrl() {
        def queryString = '';
        if (project.database.queryParameters != null) {
            queryString = project.database.queryParameters
        }

        if (project.database.url.endsWith('/')) {
            project.database.url + project.database.databaseName + queryString
        } else {
            project.database.url + '/' + project.database.databaseName + queryString
        }
    }

    def executeSqlFile(sql) {
        project.database.sqlFiles.each {
            File file -> executeSqlStatement(file, sql)
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
