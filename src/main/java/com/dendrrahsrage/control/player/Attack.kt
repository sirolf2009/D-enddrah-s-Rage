package com.dendrrahsrage.control.player

import com.dendrrahsrage.control.HealthControl
import com.dendrrahsrage.entity.EntityPlayer
import com.jme3.bullet.control.GhostControl
import com.jme3.scene.Node

class Attack(
    val player: EntityPlayer,
    val ghostControl: GhostControl = player.betterPlayerControl.equipedItem!!.collision.getControl(GhostControl::class.java)
) : Action {

    val targetsHit = mutableListOf<Node>()

    override fun update() {
        ghostControl.overlappingObjects.filter {
            it.userObject is Node && !targetsHit.contains(it.userObject) && it.userObject != player
        }.forEach {
            val node = it.userObject as Node
            targetsHit.add(node)
            node.getControl(HealthControl::class.java)?.let { health ->
                println("hit "+node)
                health.damage(player.betterPlayerControl.equipedItem!!.item.getDamage())
            }
        }
        if(hasAnimationFinished()) {
            player.betterPlayerControl.action = null
        }
    }

    fun hasAnimationFinished(): Boolean = player.animComposer.getCurrentAction("attack") == null

    override fun actionExecute() {}

}