package com.dendrrahsrage.entity

import com.jme3.asset.AssetManager
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.terrain.geomipmap.TerrainQuad

class Tree(
    val assetManager: AssetManager,
    terrain: TerrainQuad
) : AbstractEntity("Tree", terrain) {

    val rigidBodyControl: RigidBodyControl

    init {
        val model = assetManager.loadModel("Models/tree-lowpoly-swap.blend")
        attachChild(model)
        rigidBodyControl = RigidBodyControl(0f).also { addControl(it) }
    }

    override fun getRigidBody() = rigidBodyControl

}
