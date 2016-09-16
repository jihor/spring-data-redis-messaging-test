package ru.rgs.rnd.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy

import java.nio.charset.Charset

/**
 *
 *
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 * (С) RGS Group, http://www.rgs.ru
 * Created on 2016-06-07
 */

abstract class LogbackBaseInit extends Script {

    def fileAppenders = []

    def fileAppenders(map) { this.fileAppenders = map }

    def attachFileAppenders = {}

    def attachFileAppenders(closure) { this.attachFileAppenders = closure }

    def run() {

        initPropertiesCommon()
        initPropertiesFile()
        initPropertiesAdditional()

        println "\n===== Logging properies =====\n"
        binding.getVariables().sort().each { println "$it.key = [$it.value]" }
        println "\n===== End of logging properies =====\n"

        setupFile()
        setupJMX()
    }

    Object setupJMX() {
        jmxConfigurator('ru.rgs.equifaxCreditScore:type=LoggerManager')
    }

    def initPropertiesCommon() {
        // default log level
        def defaultLogLevelStr = "INFO";
        def logLevelStr = System.getProperty("logging.level", defaultLogLevelStr)

        // if by mistake system property "log.level" equals to empty string, Level.valueOf() will return DEBUG level
        // so this is a workaround
        if (logLevelStr.trim().isEmpty()) {
            logLevelStr = defaultLogLevelStr
        }
        setProperty("logLevel", Level.valueOf(logLevelStr))

        setProperty("node", System.getProperty("logging.node", "undefined"))
        setProperty("environment", System.getProperty("logging.environment", "undefined"))
    }

    def initPropertiesFile() {
        setProperty("enableLogToFile", System.getProperty("logging.enable.log.to.file", "true").toBoolean())
        setProperty("logPath", System.getProperty("logging.directory", "logs"))
    }

    abstract def initPropertiesAdditional()

    def setupFile() {
        if (enableLogToFile.toBoolean()) {
            fileAppenders.each { fileAppender(it.key, it.value['filename'], it.value['pattern']) }
            attachFileAppenders()
        }
    }

    def fileAppender = { name, filename, _pattern ->
        // binding is not propagated to inner scope so we must copy the variables
        def _logPath = logPath
        println "Creating File Appender with name = $name, filename = $filename, " +
                "pattern = $_pattern, logPath = $_logPath"
        appender(name, RollingFileAppender) {
            file = "$_logPath/$filename"
            rollingPolicy(FixedWindowRollingPolicy) {
                fileNamePattern = "$_logPath/$filename.%i.zip"
                minIndex = 1
                maxIndex = 5
            }
            triggeringPolicy(SizeBasedTriggeringPolicy) {
                maxFileSize = "64MB"
            }
            encoder(PatternLayoutEncoder) {
                charset = Charset.forName("UTF-8")
                pattern = _pattern
            }
        }
    }
}