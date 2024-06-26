package de.dqmme.allesodernichts.util

import net.axay.kspigot.event.unregister
import org.bukkit.event.Listener

interface ListenerHolder {
    val listeners: MutableList<Listener>

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun unregisterAllListeners() {
        listeners.forEach { it.unregister() }
    }
}
