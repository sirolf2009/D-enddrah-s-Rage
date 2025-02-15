package com.dendrrahsrage.control.player

import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.item.PlaceableItem
import com.jme3.bullet.PhysicsSpace
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.collision.CollisionResults
import com.jme3.math.Ray
import com.jme3.scene.Node
import com.jme3.terrain.geomipmap.TerrainPatch

class PlaceItem(
    val item: PlaceableItem,
    val player: EntityPlayer,
    val worldNode: Node,
) : Action {

    init {
        worldNode.attachChild(item.model)
    }

    override fun update() {
        val results = CollisionResults()
        val ray = Ray(player.camNode.camera.getLocation(), player.camNode.camera.getDirection())
        worldNode.collideWith(ray, results)

        results.find { it.geometry is TerrainPatch }?.let {
            item.model.setLocalTranslation(it.contactPoint)
        }
    }

    override fun actionExecute() {
        player.betterPlayerControl.action = null
        player.betterPlayerControl.inventory.removeItem(item)
        item.model.addControl(RigidBodyControl(0f))
        player.world!!.physicsSpace.add(item.model)
    }

}