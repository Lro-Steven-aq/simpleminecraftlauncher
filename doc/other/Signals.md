# Signals

## Logs
About logs, there are some signals below
1. log.trace
2. log.debug
3. log.info
4. log.warn
5. log.error
6. log.fatal
<br/>
Note: "log.trace" and "log.debug" will be caught by plugin-logger, but no output. (see it in log4j2.xml configuration file)

## Popups
About popups, there are some signals below
1. ui.popup.trace / ui.popup.debug / ui.popup.info  ->NONE
2. ui.popup.warn  ->WARN
3. ui.popup.error / ui.popup.fatal  ->ERROR
4. ui.popup.confirmation  ->CONFIRMATION