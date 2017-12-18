package pl.lukaszhuculak.experiments.rxjava2.experiments

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import pl.lukaszhuculak.experiments.rxjava2.*

object CreateSourceExperiment0 : Experiment<Long>() {
    override val description: CharSequence
            = "Experiment with creating custom source with create() operator"

    override fun prepareExperiment(): Observable<Long> {

        val source = Observable.create<Long> { emitter ->
            logThread("Creating source")
            var i: Long = 0
            while (!emitter.isDisposed && i < 5) {
                emitter.onNext(i)
                Thread.sleep(2000)
                i++
            }
            emitter.onComplete()
        }

        return source.withLoggers()
    }

}

object CreateSourceExperiment1 : Experiment<Long>() {
    override val description: CharSequence
            = "Experiment with creating custom source with create() operator and disposing from it"

    override fun prepareExperiment(): Observable<Long> {

        val source = Observable.create<Long> { emitter ->
            logThread("Creating source")
            var i: Long = 0
            while (!emitter.isDisposed && i < 5) {
                emitter.onNext(i)
                Thread.sleep(400)
                i++
            }
            emitter.onComplete()
        }

        val s1 = source.withLoggers()

        return rangeAsync(6)
                .logNext { "Before switchMap next: $it" }
                .switchMap {
                    if (it < 4)
                        s1
                    else
                        Observable.just(it)
                }
    }

}


private inline fun <reified T> Observable<T>.withLoggers(): Observable<T> {
    return this.subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.io())
            .logNext { "Next: $it" }
            .logSubscribing { "OnSubscribe $it" }
            .doOnComplete { logThread("OnComplete") }
            .doOnDispose { logThread("OnDispose") }
            .doOnTerminate { logThread("OnTerminate") }
}
