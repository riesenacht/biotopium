/*
 * Copyright (c) 2021 The biotopium Authors.
 * This file is part of biotopium.
 *
 * biotopium is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * biotopium is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with biotopium.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.riesenacht.biotopium.network.utils

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise

/**
 * Creates a JavaScript object with the given properties.
 * @param init init function
 * @return JavaScript object
 */
inline fun jsObject(init: dynamic.() -> Unit) {
    val obj = js("{}")
    init(obj)
    return obj
}

/**
 * Creates a JavaScript object from given pairs of properties and values.
 * @param pairs pairs of properties and values
 * @return JavaScript object
 */
fun jsObjectFromPairs(vararg pairs: Pair<Any, Any>): dynamic {
    val obj = js("{}")
    for((key, value) in pairs) {
        obj[key] = value
    }
    return obj
}

/**
 * Creates a JavaScript array containing the given items.
 * @param items items of array
 * @return JavaScript array
 */
fun jsArray(vararg items: dynamic): dynamic {
    val arr = js("[]")
    for(item in items) {
        arr.push(item)
    }
    return arr
}

/**
 * Awaits the termination of a promise.
 * This is a helper function which simulates the await keyword of JavaScript.
 */
suspend fun <T> Promise<T>.await(): T = suspendCoroutine { continuation ->
    then({ continuation.resume(it) }, { continuation.resumeWithException(it) })
}