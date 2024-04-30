package de.dqmme.allesodernichts.task.tasks

import de.dqmme.allesodernichts.game.gamePlayers
import de.dqmme.allesodernichts.task.Task
import de.dqmme.allesodernichts.util.text
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task

class ReachHeightTask(private val height: Int) : Task(text("Stehe auf Höhe $height")) {
    override fun register() {
        addTask(task(sync = false, delay = 0, 20) {
            val player = gamePlayers.firstOrNull { it.location.blockY == height }
            if (player != null) sync { finish(player) }
        }!!)
    }
}
