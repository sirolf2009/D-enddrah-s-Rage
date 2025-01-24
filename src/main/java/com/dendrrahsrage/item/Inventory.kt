package com.dendrrahsrage.item

class Inventory(
    private val items: MutableList<Item> = ArrayList(),
    private var maxWeight: Float = 40f
) {

    var currentWeight: Float

    init {
        currentWeight = items.map { it.weight }.sum()
    }

    fun addItem(item: Item): Boolean {
        if(currentWeight + item.weight > maxWeight) {
            return false
        }
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

    fun items() = this.items.toList()

}