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
package me.treyruffy.projectaurora

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraft.client.Minecraft
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder

private const val PROTOCOL = "http"
private const val HOST = "localhost"
private const val PORT = 9088

private const val TICK_RATE: Int = 20

private fun sendGameState(state: String) {
  try {
    val httpClient = HttpClientBuilder.create().build()
    val httpPost = HttpPost("$PROTOCOL://$HOST:$PORT")
    httpPost.setHeader("Content-Type", "application/json")
    httpPost.entity = StringEntity(state)
    httpClient.execute(httpPost)
  } catch (_: Exception) {
    // ignored
  }
}

private var tickCounter = 0

/**
 * Sends the data to Aurora.
 *
 * @param client the Minecraft client
 */
internal fun sendToAurora(client: Minecraft) {
  if (tickCounter < TICK_RATE) {
    tickCounter++
    return
  }

  val jsonObject = JsonObject()
  jsonObject.add(
    "provider",
    JsonObject().apply {
      addProperty("name", "minecraft")
      addProperty("appid", -1)
    },
  )

  val jsonProperties = hashMapOf<String, HashMap<String, Any>>()
  ModuleManager.get().modules().forEach { module ->
    if (module == null) {
      return@forEach
    }
    val moduleData = module.update(client) ?: return@forEach
    if (!jsonProperties.containsKey(module.category)) {
      jsonProperties[module.category] = hashMapOf()
    }
    jsonProperties[module.category]?.set(module.id, moduleData)
  }

  for ((category, properties) in jsonProperties) {
    jsonObject.add(category, Gson().toJsonTree(properties))
  }

  // Send the data to Aurora
  val stringData = Gson().toJson(jsonObject)
  sendGameState(stringData)
  tickCounter = 0
  ProjectAurora.log(ProjectAurora.LogType.INFO, "Sent data to Aurora: $stringData")
}

internal fun sendBlankToAurora() {
  val jsonObject = JsonObject()
  jsonObject.add(
    "provider",
    JsonObject().apply {
      addProperty("name", "minecraft")
      addProperty("appid", -1)
    },
  )

  val stringData = Gson().toJson(jsonObject)
  sendGameState(stringData)
  ProjectAurora.log(ProjectAurora.LogType.INFO, "Sent blank data to Aurora")
}
