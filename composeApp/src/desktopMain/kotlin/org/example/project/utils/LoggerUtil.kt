package org.example.project.utils

import java.io.File
import java.io.FileInputStream
import java.util.logging.*

object LoggerUtil {
    private val LOG: Logger = Logger.getLogger(LoggerUtil::class.java.name)

    init {
        try {
            val configFile = File("resources/logging.properties")
            if (configFile.exists()) {
                LogManager.getLogManager().readConfiguration(FileInputStream(configFile))
                println("Logger configurado desde logging.properties")
            } else {
                println("No se encontró logging.properties, usando configuración por defecto.")

                val fileHandler = FileHandler("logs/app-log.xml", true)
                fileHandler.formatter = XMLFormatter()
                LOG.addHandler(fileHandler)

                LOG.level = Level.ALL
                LOG.useParentHandlers = false
            }
        } catch (e: Exception) {
            println("Error al cargar la configuración del logger: ${e.message}")
        }
    }

    fun info(message: String) {
        LOG.info(message)
    }

    fun warning(message: String) {
        LOG.warning(message)
    }

    fun severe(message: String) {
        LOG.severe(message)
    }
}
