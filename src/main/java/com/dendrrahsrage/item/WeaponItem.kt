package com.dendrrahsrage.item

import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.gui.InventoryView
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.jme3.scene.Node

abstract class WeaponItem(
    name: String,
    model: Node,
    weight: Float,
) : Item(name, model, weight) {

    override fun contextMenuItems(
        betterPlayerControl: BetterPlayerControl,
        inventoryView: InventoryView
    ): List<ContextMenuAction> =
        listOf(
            ContextMenuAction("Equip") {
                betterPlayerControl.inventory.removeItem(this)
                betterPlayerControl.equip(this)
            }
        ) + super.contextMenuItems(betterPlayerControl, inventoryView)

    abstract fun getWalkForward(): String
    abstract fun getIdle(): String
    abstract fun getAttack(): String

    abstract fun onEquipped()

}