package com.example.viewmodelapp

import android.os.AsyncTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher

class TestCoroutineDispatchersProvider: CoroutineDispatchersProvider {
    override fun  io(): CoroutineDispatcher = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
}