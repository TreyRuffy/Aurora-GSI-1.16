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
package me.treyruffy.projectaurora.modules.player

import net.minecraft.client.Minecraft
import net.minecraft.world.effect.MobEffectInstance
import java.util.Locale

internal class PlayerEffects : PlayerBaseModule<Map<String, Boolean>>("playerEffects") {
  override fun update(client: Minecraft): Map<String, Boolean>? {
    val player = client.player ?: return null
    return player.activeEffects.toMapCamelCase()
  }
}

private fun Collection<MobEffectInstance>.toMapCamelCase(): Map<String, Boolean> =
  associate { effect ->
    val camelName =
      effect.effect.registeredName
        .replace("minecraft:", "")
        .split("_")
        .mapIndexed { index, s ->
          if (index == 0) {
            s.lowercase(Locale.getDefault())
          } else {
            s.replaceFirstChar { it.titlecase(Locale.getDefault()) }
          }
        }
    camelName.joinToString("") to true
  }
