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

package ch.riesenacht.biotopium.logging

import org.slf4j.impl.SimpleLogger

/**
 * The configuration of the logging,
 * implementation for JVM platform.
 *
 * @author Manuel Riesen
 */
actual object LoggingConfig {

    /**
     * Sets the logging [level].
     */
    actual fun setLoggingLevel(level: LoggingLevel) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, level.name)
    }
}