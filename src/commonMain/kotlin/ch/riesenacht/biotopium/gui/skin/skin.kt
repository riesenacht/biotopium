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

package ch.riesenacht.biotopium.gui.skin

import com.soywiz.korge.ui.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import com.soywiz.korim.bitmap.asNinePatchSimpleRatio
import com.soywiz.korim.bitmap.sliceWithSize
import com.soywiz.korim.color.ColorTransform
import com.soywiz.korim.color.transform
import com.soywiz.korio.util.AsyncOnce
import ch.riesenacht.biotopium.gui.primaryColor

/**
 * Neutralizes [com.soywiz.korim.color.Colors.DIMGREY].
 */
private val neutralizerTransform: ColorTransform
    get() {
        val neutralizeFactor = 1.0/(105.0/255.0)
        return ColorTransform.Multiply(neutralizeFactor, neutralizeFactor, neutralizeFactor, neutralizeFactor)
    }

/**
 * The biotopium color transform.
 */
private val biotopiumColorTransform = ColorTransform(primaryColor)

/**
 * The biotopium UI skin image.
 */
private val BIOTOPIUM_UI_SKIN_IMG by lazy {
    DEFAULT_UI_SKIN_IMG.withColorTransform(neutralizerTransform).withColorTransform(biotopiumColorTransform)
}

private val BiotopiumUISkinOnce = AsyncOnce<UISkin>()

/**
 * The biotopium UI skin.
 */
suspend fun BiotopiumUISkin(): UISkin = BiotopiumUISkinOnce {

    val ui = BIOTOPIUM_UI_SKIN_IMG

    fun <T : Bitmap> BitmapSlice<T>.ninePatch() = asNinePatchSimpleRatio(0.25, 0.25, 0.75, 0.75)

    UISkin {
        buttonNormal = ui.sliceWithSize(0, 0, 64, 64).ninePatch()
        buttonOver = ui.sliceWithSize(64, 0, 64, 64).ninePatch()
        buttonDown = ui.sliceWithSize(127, 0, 64, 64).ninePatch()
        buttonBackColor = buttonBackColor.transform(biotopiumColorTransform)
    }
}