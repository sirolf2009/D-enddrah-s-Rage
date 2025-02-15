package com.dendrrahsrage.crafting

import com.dendrrahsrage.item.Item

interface CraftingRecipe {

    fun getOutput(): List<Pair<Item, Int>>
    fun getCosts(): List<Pair<Item, Int>>

}