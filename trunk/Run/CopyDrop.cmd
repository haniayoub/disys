robocopy Executer executer2 executer.jar  /E
robocopy Executer executer3 executer.jar /E
cd executer2
start Executer.cmd
cd ..\executer3
start Executer.cmd
pause