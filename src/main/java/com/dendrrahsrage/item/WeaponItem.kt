package com.dendrrahsrage.item

import com.dendrrahsrage.entity.EntityPlayer
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
        player: EntityPlayer,
        inventoryView: InventoryView
    ): List<ContextMenuAction> =
        listOf(
            ContextMenuAction("Equip") {
                player.betterPlayerControl.inventory.removeItem(this)
                player.betterPlayerControl.equip(this)
            }
        ) + super.contextMenuItems(player, inventoryView)

    abstract fun getWalkForward(): String
    abstract fun getIdle(): String
    abstract fun getAttack(): String

    abstract fun onEquipped()

    abstract fun createCollisionShape(): Node
    fun getDamage(): Float = 10f

}
