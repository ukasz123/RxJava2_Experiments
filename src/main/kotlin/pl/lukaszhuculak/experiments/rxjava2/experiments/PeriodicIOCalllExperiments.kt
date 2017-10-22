package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.io
import pl.lukaszhuculak.experiments.rxjava2.Experiment
import pl.lukaszhuculak.experiments.rxjava2.logMessageOnError
import pl.lukaszhuculak.experiments.rxjava2.logNext
import pl.lukaszhuculak.experiments.rxjava2.logNext
import pl.lukaszhuculak.experiments.rxjava2.logOnError
import java.util.concurrent.TimeUnit

/**
 * Created by Lukasz Huculak on 22.10.2017.
 */

object BackendSunnyDayExperiment : Experiment<String>() {
    override val description: CharSequence
        get() = "Experiment with periodic webservice call that always succeeds using flatMap"

    override fun prepareExperiment(): Observable<String> {
        return Observable.intervalRange(0, 10, 2, 4, TimeUnit.SECONDS)
                .logNext { "call id: $it" }
                .flatMap { Observable.just("answer: $it") }
                .logNext { "after flatMap $it" }
    }

}

object BackendSunnyDayWithDelayFlatMapExperiment : Experiment<String>() {
    override val description: CharSequence
        get() = "Experiment with periodic webservice call that always succeeds but with a delay mapping with flatMap()"

    override fun prepareExperiment(): Observable<String> {
        return Observable.intervalRange(0, 10, 2, 5, TimeUnit.SECONDS)
                .logNext { "call id: $it" }
                .flatMap { id ->
                    Observable.timer(id * 2, TimeUnit.SECONDS, io())
                            .map { "answer: $id" }
                }
                .logNext { "after flatMap $it" }
    }

}


object BackendSunnyDayWithDelaySwitchMapExperiment : Experiment<String>() {
    override val description: CharSequence
        get() = "Experiment with periodic webservice call that always succeeds but with a delay mapping with switchMap()"

    override fun prepareExperiment(): Observable<String> {
        return Observable.intervalRange(0, 10, 2, 5, TimeUnit.SECONDS)
                .logNext { "call id: $it" }
                .switchMap { id ->
                    Observable.timer(id * 2, TimeUnit.SECONDS, io())
                            .map { "answer: $id" }
                }
                .logNext { "after switchMap  $it" }
    }

}

object BackendWithErrorsAndDelayExperiment : Experiment<String>() {
    override val description: CharSequence
        get() = "Backend calls that fails sometimes - uses retry() and never ends"

    override fun prepareExperiment(): Observable<String> {
        return Observable.intervalRange(0, 10, 2, 5, TimeUnit.SECONDS)
                .logNext { "call id: $it" }
                .flatMap { id ->
                    when (id.toInt()) {
                        4, 6, 8, 10 -> {
                            Observable.timer(id * 2, TimeUnit.SECONDS, io())
                                    .flatMap { Observable.error<String>(RuntimeException("Error on call $id")) }
                        }
                        else -> {
                            Observable.timer(id * 2, TimeUnit.SECONDS, io())
                                    .map { "answer: $id" }
                        }
                    }
                }
                .logNext { "after call: $it" }
                .logMessageOnError { "after error call: $it" }
                .retry()
    }

}


object BackendWithErrorsAndDelayExperiment1 : Experiment<String>() {
    override val description: CharSequence
        get() = "Backend calls that fails sometimes - uses onErrorReturn"

    override fun prepareExperiment(): Observable<String> {
        return Observable.intervalRange(0, 10, 2, 5, TimeUnit.SECONDS)
                .logNext { "call id: $it" }
                .flatMap { id ->
                    when (id.toInt()) {
                        4, 6, 8, 10 -> {
                            Observable.timer(id * 2, TimeUnit.SECONDS, io())
                                    .flatMap { Observable.error<String>(RuntimeException("Error on call $id")) }
                        }
                        else -> {
                            Observable.timer(id * 2, TimeUnit.SECONDS, io())
                                    .map { "answer: $id" }
                        }
                    }
                }
                .logNext { "after call: $it" }
                .logMessageOnError { "after error call: $it" }
                .onErrorReturn { "error happened: ${it.localizedMessage}" }
                .logNext { "after onErrorReturn $it" }
    }

}