package de.dqmme.allesodernichts.task

import de.dqmme.allesodernichts.task.tasks.FindItemTask
import de.dqmme.allesodernichts.task.tasks.ForceBlockTask
import de.dqmme.allesodernichts.task.tasks.ReachHeightTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import kotlin.random.Random
import kotlin.random.nextInt

object TaskManager {
    private val tasks = mutableListOf<Task>()

    operator fun invoke() {
        tasks.add(ReachHeightTask(Random.nextInt(140..250)))
        tasks.add(FindItemTask(FindItemTask.ITEMS.random()))
        tasks.add(ForceBlockTask(ForceBlockTask.BLOCKS.random()))
    }

    fun makeComponent(player: Player): Component {
        return Component.join(JoinConfiguration.separator(Component.text(" / ").color(NamedTextColor.GREEN)),
            tasks.map { it.makeComponent(player) }.toSet()
        )
    }
}
