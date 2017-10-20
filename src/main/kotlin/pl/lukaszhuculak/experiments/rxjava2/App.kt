package pl.lukaszhuculak.experiments.rxjava2

import pl.lukaszhuculak.experiments.rxjava2.experiments.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    val doneSignal = CountDownLatch(experiments.size)
    val executor = Executors.newSingleThreadExecutor()

    experiments.forEach {
        executor.execute {
            logThread("Running experiment: ${it.description}")
            it.run(doneSignal = doneSignal)
        }
    }
    doneSignal.await()
    logThread("${experiments.size} experiments completed")
}

private val experiments: Array<Experiment<*>> = arrayOf(
//        Experiment0,
//        Experiment1,
        ObservingExperiment1,
        ObservingExperiment2,
        ObservingExperiment3,
        DifferentSubscribeWithSourcesZippedExperiment,
        CreateWithDeferExperiment,
        CreateWithSwitchMapExperiment
)