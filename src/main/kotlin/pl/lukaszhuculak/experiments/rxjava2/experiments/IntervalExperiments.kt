package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.lukaszhuculak.experiments.rxjava2.Experiment
import pl.lukaszhuculak.experiments.rxjava2.logNext
import java.util.*
import java.util.concurrent.TimeUnit

private val coreNum = Runtime.getRuntime().availableProcessors()

private val timeout = Single.timer(7400, TimeUnit.MILLISECONDS, Schedulers.newThread())
        .toObservable()

private fun createGenerators(num: Int,
                             intervalGen: () -> Observable<Long> = { Observable.interval(1, TimeUnit.SECONDS) }
): List<Observable<String>> {
    val list = (0..num).map { i ->
        intervalGen()
                .map { "generator[$i]=$it" }
                .logNext { it }
    }
    return Collections.unmodifiableList(list)
}

private fun prepareIntervalExperiment(generator: () -> List<Observable<String>>) =
        Observable.merge(
                generator()
        )
                .observeOn(Schedulers.newThread())
                .logNext { "generated: $it" }
                .takeUntil(timeout)

object CoreNumIntervalDefaultScheduler : Experiment<String>() {
    override val description: CharSequence
            = "Test multiple inverval() observables running on default scheduler"

    override fun prepareExperiment(): Observable<String>
            = prepareIntervalExperiment { createGenerators(coreNum) }
}

object CoreNumTimes2IntervalDefaultScheduler : Experiment<String>() {
    override val description: CharSequence
            = "Test multiple inverval() observables running on default scheduler - twice as much as cores"

    override fun prepareExperiment(): Observable<String>
            = prepareIntervalExperiment { createGenerators(coreNum * 2) }
}

object IntervalNewThreadScheduler : Experiment<String>() {
    override val description: CharSequence
        get() = "Test multiple interval() observables running on NewThread scheduler"

    override fun prepareExperiment(): Observable<String>
            = prepareIntervalExperiment {
        createGenerators(coreNum * 2
                , { Observable.interval(1, TimeUnit.SECONDS, Schedulers.newThread()) })
    }

}