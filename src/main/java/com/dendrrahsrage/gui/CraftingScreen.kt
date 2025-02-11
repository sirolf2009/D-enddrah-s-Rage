package com.dendrrahsrage.gui

import com.dendrrahsrage.crafting.CraftingRecipe
import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.gui.contextmenu.ContextMenu
import com.dendrrahsrage.gui.layout.FlowLayout
import com.dendrrahsrage.item.Item
import com.jme3.math.Vector3f
import com.simsilica.lemur.Button
import com.simsilica.lemur.Container
import com.simsilica.lemur.component.BorderLayout
import com.simsilica.lemur.component.BorderLayout.Position
import com.simsilica.lemur.component.QuadBackgroundComponent

class CraftingScreen(
    val player: EntityPlayer,
    val recipes: List<CraftingRecipe>
) : Container(BorderLayout()) {

    val selectedRecipe: Container

    var contextMenu: ContextMenu? = null

    init {
        val recipeList = Container(FlowLayout())
        recipes.forEach { recipe ->
            Button("", elementId, style).apply {
                preferredSize = Vector3f(64f, 64f, 64f)
                background = QuadBackgroundComponent(recipe.getOutput().first().first.icon)
                addClickCommands {
                    setSelectedRecipe(recipe)
                }
            }.let {
                recipeList.addChild(it)
            }
        }

        addChild(recipeList, Position.East)

        selectedRecipe = Container(BorderLayout())
        addChild(selectedRecipe, Position.Center)

        preferredSize = Vector3f(800f, 600f, 0f)
        background = QuadBackgroundComponent()
    }

    fun setSelectedRecipe(recipe: CraftingRecipe) {
        selectedRecipe.clearChildren()
        selectedRecipe.addChild(Container(FlowLayout()).also { costs ->
            recipe.getCosts().forEach { costWithAmount ->
                Button(costWithAmount.getCostLabel(), elementId, style).apply {
                    preferredSize = Vector3f(64f, 64f, 64f)
                    background = QuadBackgroundComponent(costWithAmount.first.icon)
                    costs.addChild(this)
                }
            }
        }, Position.West)
        selectedRecipe.addChild(Container(FlowLayout()).also { output ->
            recipe.getOutput().forEach { outputWithAmount ->
                Button(outputWithAmount.getOutputLabel(), elementId, style).apply {
                    preferredSize = Vector3f(64f, 64f, 64f)
                    background = QuadBackgroundComponent(outputWithAmount.first.icon)
                    output.addChild(this)
                }
            }
        }, Position.East)
        selectedRecipe.addChild(Button("Craft!").also {
            it.addClickCommands {
                val inventory = player.betterPlayerControl.inventory
                val missingItem = recipe.getCosts().find { costWithAmount -> costWithAmount.first.getInventoryCount() < costWithAmount.second }
                if(missingItem == null) {
                    recipe.getCosts().forEach { costWithAmount ->
                        (0..costWithAmount.second).forEach { inventory.removeItem(costWithAmount.first) }
                    }
                    recipe.getOutput().forEach { outputWithAmount ->
                        (0..outputWithAmount.second).forEach { inventory.addItem(outputWithAmount.first) }
                    }
                }
            }
        }, Position.South)
    }

    fun Pair<Item, Int>.getCostLabel(): String {
        return "${this.first.getInventoryCount()}/${this.second}"
    }
    fun Pair<Item, Int>.getOutputLabel(): String {
        if(this.second == 1) {
            return ""
        }
        return "x ${this.second}"
    }

    fun Item.getInventoryCount() = player.betterPlayerControl.inventory.items().filter { it == this }.count()

    fun cleanup() {
        contextMenu?.removeFromParent()
    }

}