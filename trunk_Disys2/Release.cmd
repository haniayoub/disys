@echo off

set PATH=%PATH%;"C:\Program Files\Java\jdk1.6.0_20\bin"
set BuildDir=Release
set BuildList=src/bin/AutoUpdate src/bin/Client   src/bin/Common  src/bin/Executor  src/bin/Networking   src/bin/SystemManager  src/bin/UI  src/bin/WorkersSystem  src/bin/FailureDetector

Echo Building  SDK...
jar cvf %BuildDir%\disysSDK\disysSDK.jar -C  src\bin .
jar cvfe %BuildDir%\SystemManager\SystemManager.jar diSys.SystemManager.SystemManager -C src\bin .
jar cvfe %BuildDir%\Executer\Executer.jar diSys.Executor.ExecuterSystem -C src\bin .
jar cvfe %BuildDir%\UI\DisysUI.jar diSys.UI.SystemManagerUI -C src\bin .
jar cvfe %BuildDir%\FailureDetector\FailureDetector.jar diSys.FailureDetector.FailureDetectorSystem -C src\bin .

copy %BuildDir%\disysSDK\disysSDK.jar  ..\DistributedRegression\jars
