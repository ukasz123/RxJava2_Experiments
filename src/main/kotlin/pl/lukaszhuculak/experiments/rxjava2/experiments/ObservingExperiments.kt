package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import pl.lukaszhuculak.experiments.rxjava2.Experiment
import pl.lukaszhuculak.experiments.rxjava2.logNext
import pl.lukaszhuculak.experiments.rxjava2.range
import java.util.concurrent.TimeUnit

object ObservingExperiment1 : Experiment<String>() {
    override val description: CharSequence
        get() = "Change scheduler with observeOn"

    override fun prepareExperiment(): Observable<String> =
            range(5).map { "obsExp1[$it]" }
                    .logNext { "on initial scheduler: $it" }
                    .observeOn(Schedulers.computation())
                    .logNext { "on computation scheduler: $it" }
                    .observeOn(Schedulers.io())
                    .logNext { "on io scheduler: $it" }

}

object ObservingExperiment2 : Experiment<List<String>>() {
    override val description: CharSequence
        get() = "Change scheduler with buffer() operator"

    override fun prepareExperiment(): Observable<List<String>> {
        return range(5).map { "obsExp2[$it]" }
                .logNext { "on initial scheduler: $it" }
                .buffer(1, TimeUnit.SECONDS, 2)
                .logNext { "on buffer's scheduler: $it" }
    }
}

object ObservingExperiment3 : Experiment<String>() {
    override val description: CharSequence
        get() = "Change scheduler with subscribeWith"

    override fun prepareExperiment(): Observable<String> {
        return range(5).map { "obsExp3[$it]" }
                .subscribeOn(Schedulers.io())
                .logNext { "after subscribe on io: $it" }
                .subscribeOn(Schedulers.computation())
                .logNext { "after subscribe on computation: $it" }
    }
}