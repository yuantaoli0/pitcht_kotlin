package com.lsx.network.exception

import java.io.IOException

class ApiException(val errorCode:Int,val errorMsg:String): IOException()