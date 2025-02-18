package com.dendrrahsrage

import com.dendrrahsrage.entity.Tree
import com.jme3.bounding.BoundingBox
import com.jme3.bullet.PhysicsSpace
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector2f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.terrain.Terrain
import com.jme3.terrain.geomipmap.TerrainQuad
import kotlin.random.Random

data class World(
    val application: DendrrahsRage,
    val terrain: TerrainQuad,
    val physicsSpace: PhysicsSpace,
    val rootNode: Node
) {

    fun spawnTrees() {
        val bb = (terrain.worldBound as BoundingBox)
        val width = bb.xExtent * 2
        val height = bb.zExtent * 2
        (0..500).forEach {
            val x = (Random.nextFloat() * width) - (width/2)
            val z = (Random.nextFloat() * height) - (height/2)
            val terrainHeight = terrain.getHeight(Vector2f(x, z))
            val model = if (terrainHeight > 200) {
                application.assetManager.loadModel("Models/Tree3.glb")
            } else
                application.assetManager.loadModel("Models/tree-lowpoly-swap.glb")
            val tree = Tree(model, terrain)
            tree.setLocationOnTerrain(x, z, -1f)
            rootNode.attachChild(tree)
            physicsSpace.add(tree)
        }
    }

}