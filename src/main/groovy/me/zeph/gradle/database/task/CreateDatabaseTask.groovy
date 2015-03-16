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

        def sql = getSqlInstance()

        executeSqlFile(sql)

        sql.close()
    }

    def getSqlInstance() {
        def sql = Sql.newInstance(project.database.url,
                project.database.username,
                project.database.password,
                project.database.driver)
        println sql.connection.catalog
        sql
    }

    def executeSqlFile(sql) {
        project.database.sqlFiles.each {
            File file ->
                executeSqlStatement(file, sql)
        }
    }

    def executeSqlStatement(File file, sql) {
        file.text.split(';').each {
            def sqlStatement = it.trim()
            if (!sqlStatement.isEmpty()) {
                try {
                    sql.execute(sqlStatement)
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
