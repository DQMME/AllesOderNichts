package de.dqmme.allesodernichts.listeners

import de.dqmme.allesodernichts.scoreboard.GameScoreboard
import de.dqmme.allesodernichts.timer.Timer
import de.dqmme.allesodernichts.util.Constants
import de.dqmme.allesodernichts.util.mini
import de.dqmme.allesodernichts.util.prefix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.server
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.time.Duration.Companion.seconds

object LobbyListeners {

    fun register() {
        listen<PlayerJoinEvent> {
            it.joinMessage(handleAutomaticGameStart(true, it.player))
        }

        listen<PlayerQuitEvent> {
            it.quitMessage(handleAutomaticGameStart(false, it.player))
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

    private val scope = CoroutineScope(Dispatchers.IO)
    private var startJob: Job? = null

    private fun handleAutomaticGameStart(join: Boolean, player: Player): Component {
        if (Timer.isRunning()) {
            return prefix.append(mini("<aqua>${player.name} <green>hat das Spiel betreten."))
        }

        val onlinePlayers = server.onlinePlayers
        val onlinePlayersCount = if (join) onlinePlayers.size else onlinePlayers.size - 1
        val requiredPlayers = 2

        val playerCountComponent = mini("<gray>[<yellow>$onlinePlayersCount<aqua>/<yellow>$requiredPlayers<gray>]")

        val message = if(join) {
            prefix.append(
                mini("<aqua>${player.name} <green>hat das Spiel betreten. ")
                    .append(playerCountComponent)
            )
        } else {
            prefix.append(
                mini("<aqua>${player.name} <red>hat das Spiel verlassen. ")
                    .append(playerCountComponent)
            )
        }

        if (onlinePlayersCount < requiredPlayers) {
            if (startJob == null) {
                return message
            }

            startJob?.cancel()
            startJob = null

            onlinePlayers.forEach {
                it.exp = 0f
                it.level = 0
                it.sendMessage(prefix.append(mini("<red>Der Timer wurde abgebrochen.")))
            }

            return message
        }

        if(startJob == null) {
            onlinePlayers.forEach {
                it.sendMessage(prefix.append(mini("<green>Das Spiel startet in <yellow>60 <green>Sekunden.")))
            }

            startJob = scope.launch {
                var seconds = 0
                var progress = 0f

                while (isActive) {
                    seconds++
                    progress += (seconds / 60)

                    val timeMessage = listOf(50, 40, 30, 20, 10, 5, 3, 2, 1)

                    server.onlinePlayers.forEach {
                        it.exp = 1f
                        it.level = 60 - seconds

                        if (timeMessage.contains(60 - seconds)) {
                            it.sendMessage(
                                prefix.append(
                                    mini("<green>Das Spiel startet in <yellow>${60 - seconds} <green>Sekunden.")
                                )
                            )
                        }

                        if(seconds >=60) {
                            task(true) {
                                Timer.start()
                                GameListeners.register()
                                server.worlds.firstOrNull { world -> world.name == "world" }
                                    ?.worldBorder?.size = Constants.WORLDBORDER_SIZE
                                broadcast(prefix.append(Component.text("Das Spiel wurde gestartet!").color(NamedTextColor.GREEN)))

                            }

                            startJob = null
                            cancel()
                        }
                    }

                    delay(1.seconds)
                }
            }
        }

        return message
    }
}
