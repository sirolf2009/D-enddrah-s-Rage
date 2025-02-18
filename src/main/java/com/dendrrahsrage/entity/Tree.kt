package com.dendrrahsrage.entity

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.scene.Spatial
import com.jme3.terrain.geomipmap.TerrainQuad

class Tree(
    spatial: Spatial,
    terrain: TerrainQuad
) : AbstractEntity("Tree", terrain) {

    val rigidBodyControl: RigidBodyControl

    init {
        attachChild(spatial)
        rigidBodyControl = RigidBodyControl(
            CapsuleCollisionShape(2f, 6f),
            0f
        ).also { addControl(it) }
    }

    override fun getRigidBody() = rigidBodyControl

}
