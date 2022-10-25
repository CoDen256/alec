package coden.alec.bot.context


open class ContextProxy<T: Any>(
    private val contextSupplier: () -> Context,
    private val factory: (Context) -> T
){
    protected fun withContext(block: T.() -> Unit) {
        return block(factory(contextSupplier()))
    }
}