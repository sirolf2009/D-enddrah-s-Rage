package com.dendrrahsrage.item

import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.control.FoodControl
import com.dendrrahsrage.gui.InventoryView
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.jme3.bullet.PhysicsSpace
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.jme3.texture.Texture

open class Item(
    val name: String,
    val model: Node,
    val icon: Texture,
    val weight: Float,
) {

    open fun contextMenuItems(player: EntityPlayer, inventoryView: InventoryView) = listOf(
        ContextMenuAction("Drop") {
            player.betterPlayerControl.inventory.removeItem(this)
            spawnItem(player.parent, player.betterPlayerControl.physicsSpace).apply {
                this.getControl(RigidBodyControl::class.java).physicsLocation = player.worldTranslation
                    .add(player.betterPlayerControl.viewDirection.normalize())
                    .add(Vector3f.UNIT_Y)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        if (weight != other.weight) return false
        if (name != other.name) return false
        if (model.name != other.model.name) return false
        if (icon != other.icon) return false

        return true
    }

    override fun hashCode(): Int {
        var result = weight.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + icon.hashCode()
        return result
    }

}
