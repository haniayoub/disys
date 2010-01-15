::
:: Copyright (C) 1997-2009 Intel Corporation. All rights reserved.
:: The information and source code contained herein is the exclusive
:: property of Intel Corporation and may not be disclosed, examined
:: or reproduced in whole or in part without explicit written authorization
:: from the company.
::

cd Release/Executer
call Executer.cmd
cd ../..

cd Release/SystemManager
call SystemManager.cmd
cd ../..

cd Release/UI
java -jar DisysUI.jar
cd ../..
