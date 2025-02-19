package com.dendrrahsrage.item

import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.gui.InventoryView
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.jme3.bullet.PhysicsSpace
import com.jme3.scene.Node
import com.jme3.texture.Texture

open class FoodItem(
    name: String,
    model: Node,
    icon: Texture,
    weight: Float,
    val nutrition: Float
) : Item(name, model, icon, weight) {

    override fun contextMenuItems(player: EntityPlayer, inventoryView: InventoryView) =
        listOf(
            ContextMenuAction("Eat") {
                player.betterPlayerControl.inventory.removeItem(this)
                player.betterPlayerControl.hunger += nutrition
            }
        ) + super.contextMenuItems(player, inventoryView)

}
