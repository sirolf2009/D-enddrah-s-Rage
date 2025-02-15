package com.dendrrahsrage.entity

import com.jme3.asset.AssetManager
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.scene.Node
import com.jme3.terrain.geomipmap.TerrainQuad
import kotlin.random.Random

class Tree(
    val assetManager: AssetManager,
    terrain: TerrainQuad
) : AbstractEntity("Tree", terrain) {

    val rigidBodyControl: RigidBodyControl

    init {
        val model = if (Random.nextInt(2) == 0) assetManager.loadModel("Models/Tree3.glb") else assetManager.loadModel("Models/tree-lowpoly-swap.glb")
        attachChild(model)
        rigidBodyControl = RigidBodyControl(
            CapsuleCollisionShape(1f, 6f),
            0f
        ).also { addControl(it) }
    }

    override fun getRigidBody() = rigidBodyControl

}
