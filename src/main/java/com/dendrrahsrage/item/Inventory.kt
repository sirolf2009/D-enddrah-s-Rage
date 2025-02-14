package com.dendrrahsrage.item

import com.simsilica.lemur.core.VersionedList

class Inventory(
    private val items: VersionedList<Item> = VersionedList(),
    private var maxWeight: Float = 40f
) {

    var currentWeight: Float

    init {
        currentWeight = items.map { it.weight }.sum()
    }

    fun addItem(item: Item): Boolean {
        currentWeight += item.weight
        return items.add(item)
    }

    fun removeItem(item: Item): Boolean {
        if(items.remove(item)) {
            currentWeight -= item.weight
            return true
        }
        return false
    }

    fun items() = this.items

}