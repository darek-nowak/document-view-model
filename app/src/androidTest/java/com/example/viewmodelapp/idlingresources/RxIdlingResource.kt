package com.example.viewmodelapp.idlingresources

import androidx.test.espresso.IdlingResource
import io.reactivex.functions.Function
import java.util.concurrent.locks.ReentrantReadWriteLock

class RxIdlingResource : IdlingResource, Function<Runnable, Runnable> {

    // Guarded by rwLock
    private var transitionCallback: IdlingResource.ResourceCallback? = null
    private var tasksCounter = 0

    private val name = RxIdlingResource::class.java.simpleName
    private val rwLock = ReentrantReadWriteLock()
    //private val counter = AtomicInteger(0)

    override fun getName(): String = name

    override fun isIdleNow(): Boolean {
        rwLock.readLock().lock()
        val result: Boolean = tasksCounter == 0
        rwLock.readLock().unlock()
        return result
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        rwLock.writeLock().lock()
        transitionCallback = callback
        rwLock.writeLock().unlock()
    }

    @Throws(Exception::class)
    override fun apply(runnable: Runnable): Runnable {
        return Runnable {
            rwLock.writeLock().lock()
            tasksCounter++
            rwLock.writeLock().unlock()

            try {
                runnable.run()
            } finally {
                rwLock.writeLock().lock()

                try {
                    tasksCounter--
                    if (tasksCounter == 0 && transitionCallback != null) {
                        transitionCallback!!.onTransitionToIdle()
                    }
                } finally {
                    rwLock.writeLock().unlock()
                }
            }
        }
    }
}