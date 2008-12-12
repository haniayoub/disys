set PATH=%PATH%;C:\Program Files\Java\jdk1.6.0_07\bin
set BuildDir=Release
set BuildList=bin/AutoUpdate bin/Client   bin/Common  bin/Executor  bin/Networking   bin/SystemManager  bin/UI  bin/WorkersSystem
rmdir %BuildDir% /S /Q
mkdir %BuildDir%
mkdir %BuildDir%\SysManager
mkdir %BuildDir%\Executer
Echo Build SDK
jar cvf %BuildDir%\disysSDK.jar -C  bin .
jar cvfe %BuildDir%\SysManager\SystemManager.jar diSys.SystemManager.SystemManager -C bin .
jar cvfe %BuildDir%\Executer\Executer.jar diSys.Executor.ExecuterSystem -C bin .
pause