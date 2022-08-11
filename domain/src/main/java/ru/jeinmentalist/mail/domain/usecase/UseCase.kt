package ru.jeinmentalist.mail.domain.usecase


import kotlinx.coroutines.*
import ru.jeinmentalist.mail.domain.type.Either
import ru.jeinmentalist.mail.domain.type.exception.Failure
import kotlin.coroutines.CoroutineContext

abstract class UseCase<Params, Type> {

    private var backgroundContext: CoroutineContext = Dispatchers.IO
    private var foregroundContext: CoroutineContext = Dispatchers.Main
    private var parentJob: Job = Job()

    //    abstract suspend fun run(params: Params): Either<Failure, Type>
    abstract suspend fun run(params: Params): Either<Failure, Type>
    // пример использования
//    override suspend fun run(params: Params): Either<Failure, None> {
//        return repository.register(params.email, params.name, params.password)
//    }


    //    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        unsubscribe()
        parentJob = Job()

        CoroutineScope(foregroundContext + parentJob).launch {
            val result = withContext(backgroundContext) {
                run(params)
            }
            onResult(result)
        }
    }

    fun unsubscribe() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }
}
