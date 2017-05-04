set JAVA_OPT=-DDD_MSB_LOG_PRIORITY=debug -DDD_MSB_LOGPATH=c:\temp  -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4jLogDelegateFactory  -Dhazelcast.logging.type=log4j
java %JAVA_OPT% -cp ../conf;../lib/* eu.openmos.msb.recipesmanagement.RD_startws
pause
