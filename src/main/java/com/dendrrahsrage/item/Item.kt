package com.dendrrahsrage.item

import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.control.FoodControl
import com.dendrrahsrage.gui.InventoryView
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.jme3.bullet.PhysicsSpace
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.scene.Node

open class Item(
    val name: String,
    val model: Node,
    val weight: Float,
) {

    open fun contextMenuItems(betterPlayerControl: BetterPlayerControl, inventoryView: InventoryView) = listOf(
        ContextMenuAction("Drop") {
            betterPlayerControl.inventory.removeItem(this)
            spawnItem(betterPlayerControl.characterNode.parent, betterPlayerControl.physicsSpace).apply {
                this.getControl(RigidBodyControl::class.java).physicsLocation = betterPlayerControl.characterNode.worldTranslation
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