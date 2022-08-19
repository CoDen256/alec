package coden.alec.console

import coden.alec.app.ListScalesController
import coden.alec.data.ScaleGateway

class AlecConsole(
    private val listScalesController: ListScalesController
    )
{


    fun launch(){
        while (true){
            val input = readLine() ?: return
            if (input.startsWith("list_scales")){
                listScalesController.handle()
            }
        }
    }

}