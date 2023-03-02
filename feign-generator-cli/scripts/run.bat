@echo off

set p1=%1
set allparam=

:param
set str=%1
if "%str%"=="" (
    goto end
)
set allparam=%allparam% %str%
shift /0
goto param

:end
if "%allparam%"=="" (
    goto eof
)

rem remove left right blank
:intercept_left
if "%allparam:~0,1%"==" " set "allparam=%allparam:~1%"&goto intercept_left

:intercept_right
if "%allparam:~-1%"==" " set "allparam=%allparam:~0,-1%"&goto intercept_right

:eof

java -classpath feign-generator-cli.jar org.net5ijy.cloud.feign.generator.cli.CheckArgument %allparam%

if %errorlevel% equ 0 (
jar xvf %p1%
java -classpath feign-generator-cli.jar org.net5ijy.cloud.feign.generator.cli.FeignClientCodeGenerator %allparam%
)

pause