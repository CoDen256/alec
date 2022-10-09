//package coden.alec.console
//
//import coden.alec.app.fsm.HelpCommand
//import coden.alec.app.fsm.TextCommand
//import coden.alec.console.menu.ConsoleMenuViewer
//import coden.alec.console.view.ConsoleView
//import coden.alec.ui.menu.MenuNavigatorFactory
//import coden.fsm.StateExecutor
//
//class ConsoleApp(
//    private val view: ConsoleView,
//    private val stateExecutor: StateExecutor,
//    private val menuFactory: MenuNavigatorFactory
//    ) {
//
//    fun start() {
//        val menuViewer = ConsoleMenuViewer(menuFactory.mainMenuNavigator())
//        view.displayMainMenu(menuViewer.createMain())
//        stateExecutor.submit(HelpCommand)
//        while (true) {
//            val input = readLine() ?: break
//            if (input.startsWith("/")){
//                if (input.startsWith("/help")){
//                    stateExecutor.submit(HelpCommand)
//                }
//                if (input.startsWith("/text")){
//                    stateExecutor.submit(TextCommand(input.split("/text ")[1]))
//                }
//            }else {
//                val (message, action) = menuViewer.navigate(input)
//                action?.let {
//                    stateExecutor.submit(action)
//                }
//                view.displayMessage(message)
//            }
//        }
//    }
//
//}
//
