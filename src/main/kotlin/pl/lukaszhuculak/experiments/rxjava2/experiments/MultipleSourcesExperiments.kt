package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import pl.lukaszhuculak.experiments.rxjava2.Experiment
import pl.lukaszhuculak.experiments.rxjava2.logNext
import pl.lukaszhuculak.experiments.rxjava2.range

/**
 * Created by Lukasz Huculak on 18.10.2017.
 */
object DifferentSubscribeWithSourcesZippedExperiment: Experiment<Pair<String, String>>() {
    override val description: CharSequence
        get() = "Two sources that declares different scheduler for subscribing zipped into one"

    override fun prepareExperiment(): Observable<Pair<String, String>> {
        val s1 = range(5).map { "S1[$it]" }
                .subscribeOn(Schedulers.io())
                .logNext { "initially source 1: $it" }
        val s2 = range(5).map { "S2[$it]" }
                .subscribeOn(Schedulers.computation())
                .logNext { "initially source 2: $it" }

        return Observables.zip(s1, s2, {v1, v2 -> Pair(v1,v2)})
                .logNext { "zipped: $it" }
    }
}