package de.dqmme.allesodernichts.util

import de.dqmme.allesodernichts.game.playerPoints
import net.axay.kspigot.extensions.onlinePlayers
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import java.util.UUID
import net.kyori.adventure.bossbar.BossBar as AdventureBossbar

object Bossbar {
    private val bossBars = hashMapOf<UUID, AdventureBossbar>()

    fun tick(time: Component) {
        onlinePlayers.filter { !bossBars.contains(it.uniqueId) }.forEach {
            val bossBar =
                AdventureBossbar.bossBar(text(""), 0f, AdventureBossbar.Color.GREEN, AdventureBossbar.Overlay.PROGRESS)
            it.showBossBar(bossBar)
            bossBars[it.uniqueId] = bossBar
        }

        bossBars.forEach { bossBarEntry ->
            Bukkit.getPlayer(bossBarEntry.key)?.showBossBar(bossBarEntry.value)
            val points = playerPoints[bossBarEntry.key] ?: 0
            val placement = playerPoints.filter { it.value > points }.count() + 1
            val progress = points.toFloat() / Constants.MAX_POINTS.toFloat()
            bossBarEntry.value.progress(progress)
            bossBarEntry.value.name(
                time.color(NamedTextColor.GOLD)
                    .append(mini(" <gray> - <green>$points/${Constants.MAX_POINTS} Punkte (<red>Platz $placement<green>)"))
            )
        }
    }
}
