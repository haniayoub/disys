@echo off
IF [%1]==[] (
echo Bad Arguments
echo   Usage: SystemManager.cmd [port]
) ELSE ( 
start javaw -jar SystemManager.jar %1
echo System Manager started
)