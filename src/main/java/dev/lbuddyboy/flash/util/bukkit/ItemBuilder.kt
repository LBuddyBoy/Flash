package dev.lbuddyboy.flash.util.bukkit

import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class ItemBuilder {
    private var `is`: ItemStack

    constructor(`is`: ItemStack) {
        this.`is` = `is`
    }

    @JvmOverloads
    constructor(m: Material?, amount: Int = 1) {
        `is` = ItemStack(m, amount)
    }

    constructor(m: Material?, amount: Int, durability: Byte) {
        `is` = ItemStack(m, amount, durability.toShort())
    }

    fun copy(): ItemBuilder {
        return ItemBuilder(`is`)
    }

    fun setDurability(dur: Short): ItemBuilder {
        `is`.durability = dur
        return this
    }

    fun setDurability(dur: Int): ItemBuilder {
        `is`.durability = dur.toShort()
        return this
    }

    fun setName(name: String?): ItemBuilder {
        val im = `is`.itemMeta
        im.displayName = CC.translate(name)
        `is`.itemMeta = im
        return this
    }

    fun setName(name: String?, vararg objects: Any?): ItemBuilder {
        val im = `is`.itemMeta
        im.displayName = CC.translate(name, *objects)
        `is`.itemMeta = im
        return this
    }

    fun addUnsafeEnchantment(ench: Enchantment?, level: Int): ItemBuilder {
        if (level < 1) {
            return this
        }
        `is`.addUnsafeEnchantment(ench, level)
        return this
    }

    fun removeEnchantment(ench: Enchantment?): ItemBuilder {
        `is`.removeEnchantment(ench)
        return this
    }

    fun setOwner(owner: String?): ItemBuilder {
        try {
            val im = `is`.itemMeta as SkullMeta
            im.owner = owner
            `is`.itemMeta = im
        } catch (ignored: Exception) {
        }
        return this
    }

    fun addEnchant(ench: Enchantment?, level: Int): ItemBuilder {
        if (level < 1) {
            return this
        }
        val im = `is`.itemMeta
        im.addEnchant(ench, level, true)
        `is`.itemMeta = im
        return this
    }

    fun addEnchantment(ench: Enchantment?, level: Int): ItemBuilder {
        if (level < 1) {
            return this
        }
        `is`.addEnchantment(ench, level)
        return this
    }

    fun addEnchantments(enchantments: Map<Enchantment?, Int?>?): ItemBuilder {
        `is`.addEnchantments(enchantments)
        return this
    }

    fun setInfinityDurability(): ItemBuilder {
        `is`.durability = Short.MAX_VALUE
        return this
    }

    fun setLore(vararg lore: String?): ItemBuilder {
        val im = `is`.itemMeta
        im.lore = CC.translate(Arrays.asList(*lore))
        `is`.itemMeta = im
        return this
    }

    fun setLore(lore: List<String?>?): ItemBuilder {
        val im = `is`.itemMeta
        im.lore = CC.translate(lore)
        `is`.itemMeta = im
        return this
    }

    fun setLore(lore: List<String?>?, vararg objects: Any?): ItemBuilder {
        val im = `is`.itemMeta
        im.lore = CC.translate(lore, *objects)
        `is`.itemMeta = im
        return this
    }

    fun removeLoreLine(line: String): ItemBuilder {
        val im = `is`.itemMeta
        val lore: MutableList<String> = ArrayList(im.lore)
        if (!lore.contains(line)) return this
        lore.remove(line)
        im.lore = lore
        `is`.itemMeta = im
        return this
    }

    fun removeLoreLine(index: Int): ItemBuilder {
        val im = `is`.itemMeta
        val lore: List<String> = ArrayList(im.lore)
        if (index < 0 || index > lore.size) return this
        lore.removeAt(index)
        im.lore = lore
        `is`.itemMeta = im
        return this
    }

    fun addLoreLine(line: String?): ItemBuilder {
        val im = `is`.itemMeta
        var lore: MutableList<String?> = ArrayList()
        if (im.hasLore()) lore = ArrayList(im.lore)
        lore.add(CC.translate(line))
        im.lore = lore
        `is`.itemMeta = im
        return this
    }

    fun insertLoreLine(line: String, pos: Int): ItemBuilder {
        val im = `is`.itemMeta
        val lore: MutableList<String> = ArrayList(im.lore)
        lore[pos] = line
        im.lore = lore
        `is`.itemMeta = im
        return this
    }

    fun setLeatherArmorColor(color: Color?): ItemBuilder {
        try {
            val im = `is`.itemMeta as LeatherArmorMeta
            im.color = color
            `is`.itemMeta = im
        } catch (expected: ClassCastException) {
        }
        return this
    }

    fun create(): ItemStack {
        return `is`
    }
}