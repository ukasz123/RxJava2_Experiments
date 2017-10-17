package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import pl.lukaszhuculak.experiments.rxjava2.Experiment
import pl.lukaszhuculak.experiments.rxjava2.logNext
import pl.lukaszhuculak.experiments.rxjava2.range

/**
 * Created by Lukasz Huculak on 17.10.2017.
 */
object Experiment1 : Experiment<String>() {
    override val description: CharSequence
        get() = "Subscribe on newThread and observe on single with sync source"

    override fun prepareExperiment(): Observable<String> {
        val o1 = range(5)
                .map { "o1:$it" }

        return o1
                .subscribeOn(Schedulers.newThread())
                .logNext { "o1 onNext: $it" }
                .observeOn(Schedulers.single())
                .logNext { "o1 onNext: $it" }

    }
}