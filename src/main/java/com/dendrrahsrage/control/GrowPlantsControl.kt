/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.control

import com.dendrrahsrage.item.FoodItem
import com.dendrrahsrage.item.Items
import com.jme3.asset.AssetManager
import com.jme3.bounding.BoundingBox
import com.jme3.bullet.PhysicsSpace
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.Node
import com.jme3.scene.control.AbstractControl
import com.jme3.terrain.geomipmap.TerrainQuad
import java.util.function.Supplier
import kotlin.random.Random


class GrowPlantsControl(
    val plants: List<Supplier<FoodItem>>,
    val physicsSpace: PhysicsSpace
) : AbstractControl() {

    override fun controlUpdate(tpf: Float) {
        if(Random.nextInt(100) == 0) {
            grow()
        }
    }

    fun grow() {
        val plant = plants[Random.nextInt(plants.size)].get()
        val plantNode = Node(plant.name)
        plantNode.attachChild(plant.model)

        val terrain = (spatial as TerrainQuad)
        val bb = (terrain.worldBound as BoundingBox)
        val width = bb.xExtent * 2
        val height = bb.zExtent * 2
        val x = (Random.nextFloat() * width) - bb.xExtent
        val z = (Random.nextFloat() * height) - bb.zExtent
        val terrainHeight = terrain.getHeight(Vector2f(x, z))
        plantNode.setLocalTranslation(Vector3f(x, terrainHeight, z))
        plantNode.addControl(FoodControl(plant))
        plantNode.addControl(RigidBodyControl(0f))
        terrain.parent.attachChild(plantNode)
        physicsSpace.add(plantNode)
    }

    override fun controlRender(rm: RenderManager, vp: ViewPort) {
    }

    companion object {
        fun defaultPlants(assetManager: AssetManager): List<Supplier<FoodItem>> = listOf(
            Supplier {Items.Mushroom(assetManager)},
            Supplier {Items.Leek(assetManager)}
        )
    }
}
