Set CopyTo=..\diSysExample
cmd /c Release.cmd
copy Release\disyssdk.jar  %CopyTo%
copy Release\SystemManager.jar  %CopyTo%\run\SysManager\SystemManager.jar
copy Release\Executer.jar  %CopyTo%\run\Executer\Executer.jar
copy client.policy  %CopyTo%\run\Executer\client.policy
cd %CopyTo%\Run\Executer\
start Executer.cmd
cd ..\SysManager\
start SysManager.cmd
