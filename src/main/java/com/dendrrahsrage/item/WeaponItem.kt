package com.dendrrahsrage.item

import com.dendrrahsrage.Player
import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.gui.InventoryView
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.jme3.scene.Node
import com.jme3.texture.Texture

abstract class WeaponItem(
    name: String,
    model: Node,
    icon: Texture,
    weight: Float,
) : Item(name, model, icon, weight) {

    override fun contextMenuItems(
        player: Player,
        inventoryView: InventoryView
    ): List<ContextMenuAction> =
        listOf(
            ContextMenuAction("Equip") {
                player.getPlayerControl().inventory.removeItem(this)
                player.getPlayerControl().equip(this)
            }
        ) + super.contextMenuItems(player, inventoryView)

    abstract fun getWalkForward(): String
    abstract fun getIdle(): String
    abstract fun getAttack(): String

    abstract fun onEquipped()

}