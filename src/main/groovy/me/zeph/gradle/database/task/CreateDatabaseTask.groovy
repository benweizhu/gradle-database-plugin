package me.zeph.gradle.database.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


class CreateDatabaseTask extends DefaultTask {
    @TaskAction
    def createDB() {
        println project.database.message
    }
}
