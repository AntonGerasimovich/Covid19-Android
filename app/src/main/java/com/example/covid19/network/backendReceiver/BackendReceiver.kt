package com.example.covid19.network.backendReceiver

import com.example.covid19.network.result.HttpException
import com.example.covid19.network.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * Global backend receiver to fetch data.
 * @param T is the response model that you expect to receive. Totally backend can return null object
 * but this model are not null otherwise [onReceive] will not complete.
 */

open class BackendReceiver<T> {
    var onComplete: (() -> Unit)? = null
    var onError: ((error: Throwable, body: HttpException?) -> Unit)? = null
    var onReceive: ((responseModel: T) -> Unit?)? = null

    /**
     * Binds [Flow] with [CoroutineScope] to execute backend request.
     * Uses [Result] as wrapper for Retrofit2 requests
     */

    fun bind(flow: Flow<Result<T>>, scope: CoroutineScope) {
        flow.onEach { response ->
            when (response) {
                is Result.Success<T> -> {
                    onReceive?.let { it(response.value) }
                }
                is Result.Failure.Error -> {
                    onError?.let { it(response.error, null) }
                }
                is Result.Failure.HttpError -> {
                    onError?.let { it(response.error, response.error) }
                }
            }
        }.onCompletion {
            onComplete?.let { it() }
        }.flowOn(Dispatchers.IO).launchIn(scope)
    }
}

/**
 * Extension to create client-server interaction
 * @param build provides ability to work with [BackendReceiver]
 * @param T response model which you are expect to receive
 * @return [BackendReceiver]
 */

fun <T> receive(build: (BackendReceiver<T>.() -> Unit)): BackendReceiver<T> {
    val backendReceiver = BackendReceiver<T>()
    backendReceiver.build()
    return backendReceiver
}