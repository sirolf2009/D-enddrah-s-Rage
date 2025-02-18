package com.dendrrahsrage.item

import com.dendrrahsrage.control.player.PlaceItem
import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.gui.InventoryView
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.jme3.scene.Node
import com.jme3.texture.Texture

open class PlaceableItem(
    name: String,
    model: Node,
    icon: Texture,
    weight: Float,
) : Item(
    name,
    model,
    icon,
    weight
) {

    override fun contextMenuItems(player: EntityPlayer, inventoryView: InventoryView): List<ContextMenuAction> {
        return listOf(
            ContextMenuAction("Place") {
                if(player.betterPlayerControl.action == null) {
                    player.betterPlayerControl.action = PlaceItem(
                        item = this,
                        player = player,
                        worldNode = player.parent,
                    )
                }
            }
        )
    }

}