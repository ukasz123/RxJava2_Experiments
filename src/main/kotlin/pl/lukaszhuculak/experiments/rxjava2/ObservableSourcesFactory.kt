package pl.lukaszhuculak.experiments.rxjava2

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val SOURCE_EXECUTOR = Schedulers.from(Executors.newSingleThreadExecutor { Thread(it, "SourceThread") })

fun range(range: Int) = Observable.range(0, range)

fun rangeAsync(range: Int) = Observable.intervalRange(0, range.toLong(), 0, 1, TimeUnit.SECONDS, SOURCE_EXECUTOR)

