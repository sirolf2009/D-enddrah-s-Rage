package com.dendrrahsrage.item

import com.dendrrahsrage.Player
import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.control.FoodControl
import com.dendrrahsrage.gui.InventoryView
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.jme3.bullet.PhysicsSpace
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.scene.Node
import com.jme3.texture.Texture

open class Item(
    val name: String,
    val model: Node,
    val icon: Texture,
    val weight: Float,
) {

    open fun contextMenuItems(player: Player, inventoryView: InventoryView) = listOf(
        ContextMenuAction("Drop") {
            player.getPlayerControl().inventory.removeItem(this)
            spawnItem(player.node.parent, player.getPlayerControl().physicsSpace).apply {
                this.getControl(RigidBodyControl::class.java).physicsLocation = player.node.worldTranslation
            }
        }
    )

    fun spawnItem(node: Node, physicsSpace: PhysicsSpace): Node {
        val itemNode = Node(name)
        itemNode.attachChild(model)
        itemNode.addControl(RigidBodyControl(weight))
        itemNode.addControl(FoodControl(this))
        node.attachChild(itemNode)
        physicsSpace.add(itemNode)
        return itemNode
    }

}