@echo off
IF [%1]==[] (
echo Bad Arguments
echo   Usage: FailureDetector.cmd [Machine name] [Port] [Executers List File Path]
) ELSE ( 
start javaw -jar FailureDetector.jar %1 %2 %3
echo Failure Detector started
)