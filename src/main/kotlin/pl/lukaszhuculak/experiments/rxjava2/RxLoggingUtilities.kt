package pl.lukaszhuculak.experiments.rxjava2

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by Lukasz Huculak on 15.10.2017.
 */

//logging shortcuts
object Printer {
    var print: (Any?) -> Unit = { message -> println(message) }
}
// SHOULD PRINT TIMESTAMP TO LOG
const val SHOW_TIMESTAMP = false

val timestampString = if (SHOW_TIMESTAMP) "("+(System.currentTimeMillis()%100000)+")" else ""

fun logThread(message: String) = Printer.print('[' + Thread.currentThread().name + "]$timestampString: " + message)

// high-order function for converting item to log entry
inline fun <reified T> logItem(crossinline body: (T) -> String): (T) -> Unit = { logThread(body(it)) }


inline fun <reified T> Observable<T>.logNext(crossinline prepareMessage: (T) -> String): Observable<T>
        = this.doOnNext { logItem(prepareMessage)(it) }

inline fun <reified T> Observable<T>.logSubscribing(crossinline prepareMessage: (Disposable) -> String): Observable<T>
        = this.doOnSubscribe { logItem(prepareMessage)(it) }

