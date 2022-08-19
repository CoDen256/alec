//package coden.alec.bot.controllers
//
//import coden.alec.bot.handler.Handler
//
//class CreateScaleHandler (
//    private val globalPresenter: GlobalPresenter,
//    private val controller: Controller
//): Handler {
//
//    private val args = HashMap<String, Any>()
//
//    override fun handle(args: String): Boolean{
//        if (args.isEmpty()){
//            globalPresenter.displayPrompt("please args")
//            return false
//        }
//        else {
//            controller.handle(message, args)
//            return true
//        }
//    }
//
//    fun handleArguments(message: Message): Boolean{
//        message.text?.let { args.add(it) }
//        if (args.size < 3){
//            bot.send(message, "ok, now other arg:")
//            return false
//        }
//        bot.send(message, "Good: $args")
//        controller.handle(message, args)
//        return true
//    }
//
//    override fun handleCommandRequest(fullMessage: String, command: String, args: String?): Boolean {
//        args?.let {
//            controller.handle(extractArgs(it))
//        }
//    }
//
//    override fun handleArgument(args: String): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    private fun extractArgs(args: String): Map<String, Any> {
//        val headers =
//    }
//}
//
