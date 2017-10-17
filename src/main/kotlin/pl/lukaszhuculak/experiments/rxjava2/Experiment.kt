package pl.lukaszhuculak.experiments.rxjava2

import io.reactivex.Observable
import java.util.concurrent.CountDownLatch

/**
 * Created by Lukasz Huculak on 17.10.2017.
 */
abstract class Experiment<T : Any> {
    abstract val description: CharSequence
    val tag: String
        get() = javaClass.simpleName

    fun run(doneSignal: CountDownLatch) {
        prepareExperiment()
                .doFinally {
                    logThread("Experiment $tag completed")
                    doneSignal.countDown()
                }
                .subscribe()
    }

    abstract fun prepareExperiment(): Observable<T>
}