set PATH=%PATH%;C:\Program Files\Java\jdk1.6.0_07\bin
mkdir Release
jar cvfe Release\SystemManager.jar CalcExecuterDemo.SystemManagerCalcDemo -C bin .
jar cvfe Release\Client.jar CalcExecuterDemo.ClientSystemCalcDemo -C bin .
jar cvfe Release\Executer.jar CalcExecuterDemo.ExecuterSystemCalcDemo -C bin .
jar cvfe Release\CleanExit.jar CalcExecuterDemo.CleanExit -C bin .