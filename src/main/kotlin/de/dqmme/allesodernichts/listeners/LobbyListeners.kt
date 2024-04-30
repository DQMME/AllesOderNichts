package de.dqmme.allesodernichts.listeners

import de.dqmme.allesodernichts.scoreboard.GameScoreboard
import de.dqmme.allesodernichts.timer.Timer
import de.dqmme.allesodernichts.util.Constants
import de.dqmme.allesodernichts.util.mini
import de.dqmme.allesodernichts.util.prefix
import net.axay.kspigot.event.listen
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object LobbyListeners {

    fun register() {
        listen<PlayerJoinEvent> {
            it.joinMessage(prefix.append(mini("<aqua>${it.player.name} <green>hat das Spiel betreten.")))
        }

        listen<PlayerQuitEvent> {
            it.quitMessage(prefix.append(mini("<aqua>${it.player.name} <red>hat das Spiel verlassen.")))
        }

        listen<EntityDamageEvent>(EventPriority.LOW) {
            it.isCancelled = !Timer.isRunning()
        }

        listen<FoodLevelChangeEvent>(EventPriority.LOW) {
            it.isCancelled = !Timer.isRunning()
        }

        listen<BlockBreakEvent>(EventPriority.LOW) {
            it.isCancelled = !Timer.isRunning()
        }

        listen<BlockPlaceEvent>(EventPriority.LOW) {
            it.isCancelled = !Timer.isRunning()
        }

        listen<PlayerJoinEvent> {
            if (Timer.isRunning()) return@listen
            GameScoreboard.setScore(it.player, 0)
            it.player.inventory.clear()
            it.player.health = 20.0
            it.player.foodLevel = 20
        }

        listen<PlayerJoinEvent> {
            it.player.sendMessage(
                prefix.append(
                    mini(
                        "<green>Willkommen zu Alles oder Nichts. In <red>12</red> Minuten ist es deine Aufgabe am <red>meisten Punkte</red> wie möglich zu sammeln." +
                            " Die Person, die nach Ablauf der Zeit am meisten Punkte hat oder am schnellsten <red>${Constants.MAX_POINTS}</red> Punkte erreicht, gewinnt. " +
                            "Punkte kannst du bekommen, indem du Items aufsammelst oder craftest, Entities tötest, Advancements bekommst, " +
                            "Aufgaben (stehen in der Action-Bar) erledigts oder Spieler tötest. <red>Aber pass auf</red>: Wenn du stirbst, " +
                            "verlierst du <red>die Hälfte</red> deiner Punkte. Außerdem wird es mit mehr Punkten immer schwieriger: " +
                            "Du kriegst mehr Schaden, bekommst schlimme Effekte und stirbst schneller. " +
                            "Du kannst dagegen vorgehen, indem du dir Rüstung (und auch Essen) aus dem Shop (/shop) kaufst. " +
                            "Wenn alle bereit sind, kann ein Admin das Spiel mit /start starten. Viel Glück!"
                    )
                )
            )
        }
    }
}
