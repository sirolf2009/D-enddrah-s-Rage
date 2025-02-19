package com.dendrrahsrage.item

import com.dendrrahsrage.Interactable
import com.dendrrahsrage.entity.EntityPlayer
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.scene.Node

class ItemNode(
    val item: Item,
    val rigidBodyControl: RigidBodyControl = RigidBodyControl(item.weight)
): Node(item.name), Interactable {

    init {
        attachChild(item.model)
        addControl(rigidBodyControl)
    }

    override fun interact(player: EntityPlayer) {
        if(player.betterPlayerControl.inventory.addItem(item)) {
            parent.detachChild(this)
            rigidBodyControl.physicsSpace.remove(rigidBodyControl)
        }
    }

}