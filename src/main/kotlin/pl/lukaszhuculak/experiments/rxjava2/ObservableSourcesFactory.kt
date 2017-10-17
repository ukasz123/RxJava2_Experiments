package pl.lukaszhuculak.experiments.rxjava2

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Łukasz on 15.10.2017.
 */

fun range(range: Int) = Observable.range(0, range)

fun rangeInter(range: Int) = Observable.interval(range.toLong(), TimeUnit.SECONDS)

