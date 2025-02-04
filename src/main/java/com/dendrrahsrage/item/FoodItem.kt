package com.dendrrahsrage.item

import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.control.FoodControl
import com.dendrrahsrage.gui.InventoryView
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.jme3.bullet.PhysicsSpace
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.scene.Node
import com.jme3.texture.Texture

open class FoodItem(
    name: String,
    model: Node,
    icon: Texture,
    weight: Float,
    val nutrition: Float
) : Item(name, model, icon, weight) {

    override fun contextMenuItems(betterPlayerControl: BetterPlayerControl, inventoryView: InventoryView) =
        listOf(
            ContextMenuAction("Eat") {
                betterPlayerControl.inventory.removeItem(this)
                betterPlayerControl.hunger += nutrition
            }
        ) + super.contextMenuItems(betterPlayerControl, inventoryView)

}