package de.dqmme.allesodernichts.task.tasks

import de.dqmme.allesodernichts.game.gamePlayers
import de.dqmme.allesodernichts.task.Task
import de.dqmme.allesodernichts.util.text
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.BlockFace

class ForceBlockTask(private val block: Material) :
    Task(text("Stehe auf ").append(Component.translatable(block.translationKey()))) {

    companion object {
        val BLOCKS = listOf(Material.DEEPSLATE, Material.POINTED_DRIPSTONE)
    }

    override fun register() {
        addTask(task(sync = false, delay = 0, 10) {
            val player = gamePlayers.firstOrNull { it.location.block.getRelative(BlockFace.DOWN).type == block }
            sync { if (player != null) finish(player) }
        }!!)
    }
}
