@echo off

set name=Disys_Ex1

IF EXIST %name%.exe (
set java_run=%name%
) ELSE (
set java_run=javaw
)

start %java_run% -jar Executer.jar %name% 3000 3001
