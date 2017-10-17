package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import pl.lukaszhuculak.experiments.rxjava2.*

/**
 * Created by Lukasz Huculak on 17.10.2017.
 */
object Experiment0 : Experiment<String>() {
    override val description: CharSequence
            = "Synchronous source on the creators thread only"

    override fun prepareExperiment(): Observable<String> {
        val c = range(5).map { "ev-$it" }
                .logSubscribing { tag + " subscribed" }
                .doOnNext { logThread("with thread: $it") }
                .doOnNext { logItem<String> { "withLogItem: $it" } }
                .logNext { tag + " logNext: $it" }
        return c
    }
}