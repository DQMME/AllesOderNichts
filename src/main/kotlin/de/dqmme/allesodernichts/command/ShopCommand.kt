package de.dqmme.allesodernichts.command

import de.dqmme.allesodernichts.gui.armorGui
import de.dqmme.allesodernichts.timer.Timer
import de.dqmme.allesodernichts.util.mini
import de.dqmme.allesodernichts.util.prefix
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import net.axay.kspigot.gui.openGUI

class ShopCommand {

    val command = command("shop") {
        runs {
            if (!Timer.isRunning()) {
                player.sendMessage(prefix.append(mini("<red>Der Shop ist nur während der Runde verfügbar!")))
                return@runs
            }

            player.openGUI(armorGui())
        }
    }
}
