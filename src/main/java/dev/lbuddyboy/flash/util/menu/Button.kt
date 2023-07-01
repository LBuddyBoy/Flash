package dev.lbuddyboy.flash.util.menu

import dev.lbuddyboy.flash.util.Callable
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

abstract class Button {
    abstract val slot: Int
    abstract val item: ItemStack?
    open fun action(event: InventoryClickEvent) {}
    open fun clickUpdate(): Boolean {
        return false
    }

    fun cancels(): Boolean {
        return true
    }

    companion object {
        fun fromButton(slot: Int, old: Button): Button {
            return object : Button() {
                override fun getSlot(): Int {
                    return slot
                }

                override fun getItem(): ItemStack? {
                    return old.item
                }

                override fun action(event: InventoryClickEvent) {
                    old.action(event)
                }
            }
        }

        fun fromItem(stack: ItemStack?, slot: Int): Button {
            return object : Button() {
                override fun getSlot(): Int {
                    return slot
                }

                override fun getItem(): ItemStack? {
                    return stack
                }
            }
        }

        fun fromItem(stack: ItemStack?, slot: Int, callable: Callable): Button {
            return object : Button() {
                override fun getSlot(): Int {
                    return slot
                }

                override fun getItem(): ItemStack? {
                    return stack
                }

                override fun action(event: InventoryClickEvent) {
                    callable.call()
                }
            }
        }
    }
}