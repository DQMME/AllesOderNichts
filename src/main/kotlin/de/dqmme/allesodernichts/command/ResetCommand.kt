package de.dqmme.allesodernichts.command

import de.dqmme.allesodernichts.util.WorldUtil
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs

class ResetCommand {
    val command = command("reset") {
        requiresPermission("ktftw.reset")
        runs {
            WorldUtil.initializeReset()
        }
    }
}
