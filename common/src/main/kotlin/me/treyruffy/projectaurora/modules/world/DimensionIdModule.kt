/*
 * This file is part of Project Aurora Minecraft Mod, licensed under the MIT License.
 *
 * Copyright (c) 2024 TreyRuffy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.treyruffy.projectaurora.modules.world

import net.minecraft.client.Minecraft

/**
 * Gets the dimension ID.
 */
internal class DimensionIdModule : WorldBaseModule<Int>("dimensionID") {
  override fun update(client: Minecraft): Int? {
    val player = client.player ?: return null
    return player
      .level()
      .dimension()
      .location()
      .toString()
      .toDimensionId()
  }

  private enum class Dimension(
    val id: Int,
  ) {
    OVERWORLD(0),
    NETHER(-1),
    END(1),
  }

  private fun String.toDimensionId(): Int =
    when (this) {
      "minecraft:overworld" -> Dimension.OVERWORLD.id
      "minecraft:the_nether" -> Dimension.NETHER.id
      "minecraft:the_end" -> Dimension.END.id
      else -> Dimension.OVERWORLD.id
    }
}
