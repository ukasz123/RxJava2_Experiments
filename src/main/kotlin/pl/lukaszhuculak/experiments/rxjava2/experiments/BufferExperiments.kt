package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import pl.lukaszhuculak.experiments.rxjava2.Experiment
import pl.lukaszhuculak.experiments.rxjava2.logNext
import pl.lukaszhuculak.experiments.rxjava2.rangeAsync
import java.util.concurrent.TimeUnit

object BufferExperiment1 : Experiment<String>() {


    override val description: CharSequence
        get() = "Does using buffer(timespan, TimeUnit) changes scheduler"

    override fun prepareExperiment(): Observable<String> {

        val source = rangeAsync(11)

        return source.logNext { "after creation: $it" }
                .buffer(2, TimeUnit.SECONDS)
                .logNext { "buffered: $it" }
                .map { it.toString() }
    }

}