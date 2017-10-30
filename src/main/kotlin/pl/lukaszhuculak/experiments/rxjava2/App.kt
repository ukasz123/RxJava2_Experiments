package pl.lukaszhuculak.experiments.rxjava2

import io.reactivex.internal.schedulers.RxThreadFactory
import io.reactivex.plugins.RxJavaPlugins
import pl.lukaszhuculak.experiments.rxjava2.CustomSchedulers.COMPUTATION
import pl.lukaszhuculak.experiments.rxjava2.CustomSchedulers.IO
import pl.lukaszhuculak.experiments.rxjava2.CustomSchedulers.NEW_THREAD
import pl.lukaszhuculak.experiments.rxjava2.CustomSchedulers.SINGLE
import pl.lukaszhuculak.experiments.rxjava2.experiments.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    initRxSchedullers()
    val executor = Executors.newSingleThreadExecutor()

    experiments.forEach {
        val doneSignal = CountDownLatch(1)
        executor.execute {
            logThread("Running experiment: ${it.description}")
            it.run(doneSignal = doneSignal)
        }
        doneSignal.await()
    }
    logThread("${experiments.size} experiments completed")
    System.exit(0)
}

private val experiments: Array<Experiment<*>> = arrayOf(
//        Experiment0,
//        Experiment1,
//        ObservingExperiment1,
//        ObservingExperiment2,
//        ObservingExperiment3,
//        DifferentSubscribeWithSourcesZippedExperiment,
//        CreateWithDeferExperiment,
//        CreateWithSwitchMapExperiment,
//        CreateWithFlatMapExperiment,
//        BufferExperiment1,
//        BackendSunnyDayExperiment,
//        BackendSunnyDayWithDelayFlatMapExperiment,
//        BackendSunnyDayWithDelaySwitchMapExperiment,
//        BackendWithErrorsAndDelayExperiment
//        BackendWithErrorsAndDelayExperiment1,
//        CreateSourceExperiment0
        CreateSourceExperiment1
)

private object CustomSchedulers {
    val COMPUTATION = RxJavaPlugins.createComputationScheduler(RxThreadFactory("ComputationScheduller"))
    val IO = RxJavaPlugins.createIoScheduler(RxThreadFactory("IOScheduler"))
    val SINGLE = RxJavaPlugins.createSingleScheduler(RxThreadFactory("SingleScheduler"))
    val NEW_THREAD = RxJavaPlugins.createNewThreadScheduler(RxThreadFactory("NewThreadScheduler"))

}

private fun initRxSchedullers() {
    RxJavaPlugins.setInitComputationSchedulerHandler { COMPUTATION }
    RxJavaPlugins.setInitIoSchedulerHandler { IO }
    RxJavaPlugins.setInitSingleSchedulerHandler { SINGLE }
    RxJavaPlugins.setInitNewThreadSchedulerHandler { NEW_THREAD }
}