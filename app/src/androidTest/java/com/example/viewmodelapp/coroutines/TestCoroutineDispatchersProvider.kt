package com.example.viewmodelapp.coroutines

import android.os.AsyncTask
import com.example.viewmodelapp.CoroutineDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher

class TestCoroutineDispatchersProvider: CoroutineDispatchersProvider {
    override fun  io(): CoroutineDispatcher = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
}