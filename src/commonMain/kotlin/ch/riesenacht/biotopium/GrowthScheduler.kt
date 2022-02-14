/*
 * Copyright (c) 2022 The biotopium Authors.
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

package ch.riesenacht.biotopium

import ch.riesenacht.biotopium.core.time.model.Timespan
import ch.riesenacht.biotopium.core.world.model.map.Plot
import ch.riesenacht.biotopium.core.world.model.plant.PlantGrowth
import ch.riesenacht.biotopium.core.world.model.plant.growthRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

/**
 * The scheduler for updating the growth of growing plants.
 *
 * @author Manuel Riesen
 */
object GrowthScheduler {

    private val timers: MutableMap<Plot, Timer> = mutableMapOf()

    private val growthDelta = Timespan(1)

    /**
     * Represents a timer.
     * Executes the [given function][doAfter] after a [time] period.
     *
     * @param plot plot for identification
     */
    private class Timer(
        private val time: Timespan,
        private val doAfter: () -> Unit,
        plot: Plot
    ) {

        private val job: Job

        init {
            job = CoroutineScope(EmptyCoroutineContext).launch {
                delay(time.millis)
                doAfter()
                timers.remove(plot)
            }
            job.start()
        }

        fun stop() {
            job.cancel()
        }
    }

    /**
     * Registers a [plot] to watch on.
     * Automatically sends a grow-action at the next possible time.
     */
    fun watchPlot(plot: Plot) {

        if(plot.plant == null) return

        if(!timers.containsKey(plot)) {
            timers[plot] = Timer(Timespan(growthRate.seconds + growthDelta.seconds), {
                if(plot.plant?.growth == PlantGrowth.GROWN) {
                    timers[plot]?.stop()
                    timers.remove(plot)
                }
                BiotopiumClient.createGrowAction(plot)

            }, plot)
        }
    }



}