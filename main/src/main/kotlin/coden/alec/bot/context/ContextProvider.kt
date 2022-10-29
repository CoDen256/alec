package coden.alec.bot.context


abstract class ContextProvider<T: Any>{

    abstract fun getContext(): Context
    abstract fun createFromContext(context: Context): T

    protected fun withContext(block: T.() -> Unit) {
        return block(createFromContext(getContext()))
    }
}