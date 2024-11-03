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

import me.treyruffy.projectaurora.exceptions.ModuleAlreadyAddedException
import me.treyruffy.projectaurora.modules.game.ChatGuiOpenModule
import me.treyruffy.projectaurora.modules.game.ControlsGuiOpenModule
import me.treyruffy.projectaurora.modules.player.PlayerEffects
import me.treyruffy.projectaurora.modules.player.action.InGameModule
import me.treyruffy.projectaurora.modules.player.action.IsBurningModule
import me.treyruffy.projectaurora.modules.player.action.IsInWaterModule
import me.treyruffy.projectaurora.modules.player.action.IsRidingHorseModule
import me.treyruffy.projectaurora.modules.player.action.IsSneakingModule
import me.treyruffy.projectaurora.modules.player.experience.ExperienceLevelModule
import me.treyruffy.projectaurora.modules.player.experience.ExperienceMaxModule
import me.treyruffy.projectaurora.modules.player.experience.ExperienceModule
import me.treyruffy.projectaurora.modules.player.food.FoodLevelMaxModule
import me.treyruffy.projectaurora.modules.player.food.FoodLevelModule
import me.treyruffy.projectaurora.modules.player.food.SaturationLevelMaxModule
import me.treyruffy.projectaurora.modules.player.food.SaturationLevelModule
import me.treyruffy.projectaurora.modules.player.health.AbsorptionMaxModule
import me.treyruffy.projectaurora.modules.player.health.AbsorptionModule
import me.treyruffy.projectaurora.modules.player.health.ArmorMaxModule
import me.treyruffy.projectaurora.modules.player.health.ArmorModule
import me.treyruffy.projectaurora.modules.player.health.HealthModule
import me.treyruffy.projectaurora.modules.player.health.IsDeadModule
import me.treyruffy.projectaurora.modules.player.health.MaxHealthModule
import me.treyruffy.projectaurora.modules.world.DimensionIdModule
import me.treyruffy.projectaurora.modules.world.IsDayTimeModule
import me.treyruffy.projectaurora.modules.world.IsRainingModule
import me.treyruffy.projectaurora.modules.world.RainStrengthModule
import me.treyruffy.projectaurora.modules.world.WorldTimeModule
import org.apache.logging.log4j.Logger

/**
 * The main class for Project Aurora Minecraft Mod.
 */
@Suppress("LibraryEntitiesShouldNotBePublic")
object ProjectAurora {
  private var logger: Logger? = null

  /**
   * Initializes Project Aurora Minecraft Mod.
   */
  fun init(logger: Logger): ProjectAurora? {
    this.logger = logger
    this.log(LogType.INFO, "initializing...")

    val moduleManager = ModuleManager.get()

    try {
      // Player modules
      addPlayerActionModules(moduleManager)
      addPlayerExperienceModules(moduleManager)
      addPlayerFoodModules(moduleManager)
      addPlayerHealthModules(moduleManager)

      moduleManager.addModule(PlayerEffects())

      // World modules
      addWorldModules(moduleManager)

      // Game modules
      addGameModules(moduleManager)
    } catch (e: ModuleAlreadyAddedException) {
      this.log(LogType.ERROR, "failed to add module: ${e.message}", e)
      return null
    }

    this.log(LogType.INFO, "initialized successfully!")
    return this
  }

  /**
   * Destroys Project Aurora Minecraft Mod.
   */
  fun destroy() {
    this.logger = null
    ModuleManager.get().destroy()
    sendBlankToAurora()
  }

  private fun addPlayerActionModules(moduleManager: ModuleManager) {
    moduleManager.addModule(InGameModule())
    moduleManager.addModule(IsBurningModule())
    moduleManager.addModule(IsInWaterModule())
    moduleManager.addModule(IsRidingHorseModule())
    moduleManager.addModule(IsSneakingModule())
  }

  private fun addPlayerExperienceModules(moduleManager: ModuleManager) {
    moduleManager.addModule(ExperienceLevelModule())
    moduleManager.addModule(ExperienceMaxModule())
    moduleManager.addModule(ExperienceModule())
  }

  private fun addPlayerFoodModules(moduleManager: ModuleManager) {
    moduleManager.addModule(FoodLevelMaxModule())
    moduleManager.addModule(FoodLevelModule())
    moduleManager.addModule(SaturationLevelMaxModule())
    moduleManager.addModule(SaturationLevelModule())
  }

  private fun addPlayerHealthModules(moduleManager: ModuleManager) {
    moduleManager.addModule(AbsorptionMaxModule())
    moduleManager.addModule(AbsorptionModule())
    moduleManager.addModule(ArmorMaxModule())
    moduleManager.addModule(ArmorModule())
    moduleManager.addModule(HealthModule())
    moduleManager.addModule(IsDeadModule())
    moduleManager.addModule(MaxHealthModule())
  }

  private fun addWorldModules(moduleManager: ModuleManager) {
    moduleManager.addModule(DimensionIdModule())
    moduleManager.addModule(IsDayTimeModule())
    moduleManager.addModule(RainStrengthModule())
    moduleManager.addModule(IsRainingModule())
    moduleManager.addModule(WorldTimeModule())
  }

  private fun addGameModules(moduleManager: ModuleManager) {
    moduleManager.addModule(ChatGuiOpenModule())
    moduleManager.addModule(ControlsGuiOpenModule())
  }

  /**
   * The type of log message.
   */
  enum class LogType {
    /**
     * An debug log message.
     */
    DEBUG,

    /**
     * An info log message.
     */
    INFO,

    /**
     * A warning log message.
     */
    WARN,

    /**
     * An error log message.
     */
    ERROR,
  }

  /**
   * Logs a message.
   *
   * @param type the type of message
   * @param message the message
   * @param throwable the throwable
   */
  @JvmOverloads
  fun log(
    type: LogType,
    message: String,
    throwable: Throwable? = null,
  ) {
    val logger = this.logger ?: return
    // TODO: Check if needed prefix
    val prefix = "[ProjectAuroraMinecraftMod] "
    val formattedMessage = prefix + message
    when (type) {
      LogType.DEBUG -> logger.debug(formattedMessage)
      LogType.INFO -> logger.info(formattedMessage)
      LogType.WARN -> logger.warn(formattedMessage, throwable)
      LogType.ERROR -> logger.error(formattedMessage, throwable)
    }
  }

  /**
   * Gets the Project Aurora instance.
   */
  @JvmStatic
  fun get(): ProjectAurora = this
}
