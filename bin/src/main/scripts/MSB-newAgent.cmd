set JAVA_OPT=-DDD_OPENMOS_LOG_PRIORITY=debug -DDD_OPENMOS_LOGPATH=c:\temp
java %JAVA_OPT% -cp ../conf;../lib/* eu.openmos.msb.NewAgentTest
pause
