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

        def sql = Sql.newInstance(project.database.url,
                project.database.username,
                project.database.password,
                project.database.driver)

        println sql.connection.catalog

        project.database.sqlFiles.each {

        }
    }

    def registerDriver(driverName, configurationName) {
        URLClassLoader loader = GroovyObject.class.classLoader as URLClassLoader
        project.configurations[configurationName].each { File file -> loader.addURL(file.toURL()) }
        DriverManager.registerDriver(loader.loadClass(driverName).newInstance() as Driver)
    }
}
