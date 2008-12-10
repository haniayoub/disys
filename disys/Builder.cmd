set PATH=%PATH%;C:\Program Files\Java\jdk1.6.0_07\bin
set BuildDir=Release
mkdir %BuildDir%
jar cvfe %BuildDir%\SystemManager.jar SystemManager.SystemManager -C bin .
jar cvfe %BuildDir%\Client.jar CalcExecuterDemo.ClientSystemCalcDemo -C bin .
jar cvfe %BuildDir%\Executer.jar Executor.ExecuterSystem -C bin .
jar cvfe %BuildDir%\CleanExit.jar CalcExecuterDemo.CleanExit -C bin .