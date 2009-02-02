Set CopyTo=..\..\UserWorkSpace\Release
cmd /c Release.cmd
robocopy Release  %CopyTo% /E
copy client.policy  %CopyTo%\Executer\client.policy
cd %CopyTo%\Executer\
start Executer.cmd
cd ..\SysManager\
start SysManager.cmd

