package de.dqmme.allesodernichts

import de.dqmme.allesodernichts.config.Config
import de.dqmme.allesodernichts.listeners.LobbyListeners
import de.dqmme.allesodernichts.scoreboard.GameScoreboard
import de.dqmme.allesodernichts.task.TaskManager
import de.dqmme.allesodernichts.timer.Timer
import de.dqmme.allesodernichts.util.WorldUtil
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.runnables.task
import org.bukkit.Bukkit

class AllesOderNichts : KSpigot() {

    override fun load() {
        saveDefaultConfig()

        if (Config.reset) {
            WorldUtil.resetWorlds()
        }
    }

    override fun startup() {
        Timer.invoke()
        TaskManager.invoke()
        de.dqmme.allesodernichts.command.ResetCommand()
        de.dqmme.allesodernichts.command.ShopCommand()
        de.dqmme.allesodernichts.command.StartCommand()
        LobbyListeners.register()

        task(sync = true, delay = 1) {
            GameScoreboard.create()
            Bukkit.getWorld("world")!!.worldBorder.size = 30.0
        }
    }

    override fun shutdown() {
        Config.reset = true
    }
}
