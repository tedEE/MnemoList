package ru.jeinmentalist.mail.domain.type

open class HandleOnce<out T>(private val content: T) {

    private var hasBeenHandled = false

    /**
     * Возвращает содержимое и предотвращает его повторное использование.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}