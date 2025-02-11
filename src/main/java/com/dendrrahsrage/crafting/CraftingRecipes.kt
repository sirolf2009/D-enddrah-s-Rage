package com.dendrrahsrage.crafting

import com.dendrrahsrage.item.Item
import com.dendrrahsrage.item.Items
import com.jme3.asset.AssetManager

class CraftingRecipes {

    class CampfireRecipe(val assetManager: AssetManager) : CraftingRecipe {
        override fun getOutput(): List<Pair<Item, Int>> {
            return listOf(
                Pair(Items.Campfire(assetManager), 1)
            )
        }

        override fun getCosts(): List<Pair<Item, Int>> {
            return listOf(
                Pair(Items.Log(assetManager), 6)
            )
        }

    }

    companion object {

        fun getRecipes(assetManager: AssetManager) = listOf(
            CampfireRecipe(assetManager)
        )
    }

}