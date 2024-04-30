package de.dqmme.allesodernichts.command

import de.dqmme.allesodernichts.listeners.GameListeners
import de.dqmme.allesodernichts.timer.Timer
import de.dqmme.allesodernichts.util.Constants
import de.dqmme.allesodernichts.util.prefix
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.axay.kspigot.extensions.broadcast
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class StartCommand {

    val command = command("start") {
        require(!Timer.isRunning())
        requiresPermission("ktftw.start")
        runs {
            Timer.start()
            GameListeners.register()
            server.worlds.firstOrNull { it.name == "world" }
                ?.worldBorder?.size = Constants.WORLDBORDER_SIZE
            broadcast(prefix.append(Component.text("Das Spiel wurde gestartet!").color(NamedTextColor.GREEN)))
        }
    }
}
