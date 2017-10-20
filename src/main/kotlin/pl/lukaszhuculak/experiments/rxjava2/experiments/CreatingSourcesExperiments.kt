package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import pl.lukaszhuculak.experiments.rxjava2.Experiment
import pl.lukaszhuculak.experiments.rxjava2.logNext
import pl.lukaszhuculak.experiments.rxjava2.logThread
import pl.lukaszhuculak.experiments.rxjava2.range
import java.util.concurrent.Callable

/**
 * Created by Lukasz Huculak on 20.10.2017.
 */
object CreateWithDeferExperiment : Experiment<String>() {
    override val description: CharSequence
        get() = "Create observable source that declares subscribeOn() with defer() operator"

    override fun prepareExperiment(): Observable<String> {
        val supplier = Callable<ObservableSource<String>> {

            logThread("supplier called")
            range(6)
                    .subscribeOn(Schedulers.single())
                    .map { item -> "def_ev[$item]" }
                    .logNext { "created source: $it" }
        }

        return Observable.defer(supplier)
                .subscribeOn(Schedulers.io())
                .logNext { "outside supplier" }
    }
}

object CreateWithSwitchMapExperiment : Experiment<String>() {
    override val description: CharSequence
        get() = "Create observable sources that declares subscribeOn with swithMap() operator"

    override fun prepareExperiment(): Observable<String> {
        val supplier: (Int) -> ObservableSource<Int> = {

            index ->
            logThread("supplier for $index called")
            range(index).subscribeOn(Schedulers.newThread())
                    .logNext { item -> "in switch supplier: $item" }
        }
        return range(5)
                .subscribeOn(Schedulers.computation())
                .logNext { "before switchMap(): $it" }
                .switchMap(supplier)
                .logNext { "after switchMap(): $it" }
                .map { "$it" }

    }
}