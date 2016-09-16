import ch.qos.logback.classic.Level
import groovy.transform.BaseScript
import ru.rgs.rnd.logging.LogbackBaseInit

enum Loggers {
    businessOperationLogger, slowQueryLogging
}

enum Appenders {
    FileTechAppender, FileBusinessAppender, FileSlowqueryAppender
}

def init = {
    @BaseScript LogbackBaseInit logbackBaseInitScript
    fileAppenders(["$Appenders.FileTechAppender"     : [filename: "service.log", pattern: "%d{MM.dd-HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n"],
                   "$Appenders.FileBusinessAppender" : [filename: "business.log", pattern: "%d{MM.dd-HH:mm:ss.SSS} CorrelationId: %X{CorrelationId} DocumentKey: %X{DocumentKey} Action: %X{Action} System: %X{System} - %msg%n"],
                   "$Appenders.FileSlowqueryAppender": [filename: "slowquery.log", pattern: "%d{MM.dd-HH:mm:ss.SSS} CorrelationId: %X{CorrelationId} DocumentKey: %X{DocumentKey} Action: %X{Action} System: %X{System} Duration: %X{Duration} Method: %X{Method} - %msg%n"]])

    attachFileAppenders({
        root(logLevel, ["$Appenders.FileTechAppender"])
        logger("$Loggers.businessOperationLogger", Level.INFO, ["$Appenders.FileBusinessAppender"], false)
        logger("$Loggers.slowQueryLogging", Level.INFO, ["$Appenders.FileSlowqueryAppender"], false)
    })

}

init()