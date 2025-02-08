package com.dendrrahsrage.entity

import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.jme3.terrain.geomipmap.TerrainQuad

abstract class AbstractEntity(
    nodeName: String,
    val terrain: TerrainQuad,
) : Node(nodeName) {

    fun setLocationOnTerrain(x: Float, z: Float) {
        setLocalTranslation(x, terrain.getHeight(Vector2f(x, z)) + 1, z)
        getRigidBody().physicsLocation = Vector3f(x, terrain.getHeight(Vector2f(x, z)) + 1, z)
    }

    abstract fun getRigidBody(): PhysicsRigidBody

}
