set PATH=%PATH%;H:\Program Files\Java\jdk1.6.0_07\bin
set BuildDir=Release
set BuildList=bin/AutoUpdate bin/Client   bin/Common  bin/Executor  bin/Networking   bin/SystemManager  bin/UI  bin/WorkersSystem
rmdir %BuildDir% /S /Q
mkdir %BuildDir%
Echo Build SDK
jar cvf %BuildDir%\disysSDK.jar -C  bin .
jar cvfe %BuildDir%\SystemManager.jar diSys.SystemManager.SystemManager -C bin .
jar cvfe %BuildDir%\Executer.jar diSys.Executor.ExecuterSystem -C bin .
jar cvfe %BuildDir%\DisysUI.jar diSys.UI.SystemManagerUI -C bin .