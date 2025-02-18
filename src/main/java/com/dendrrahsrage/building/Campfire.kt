package com.dendrrahsrage.building

import com.jme3.asset.AssetManager
import com.jme3.effect.ParticleEmitter
import com.jme3.effect.ParticleMesh
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Node


class Campfire (
    val assetManager: AssetManager
): Node("Campfire") {

    init {
        attachChild(assetManager.loadModel("Models/campfire.glb") as Node)
        light()
    }

    fun light() {
        val fire = ParticleEmitter("smoke", ParticleMesh.Type.Triangle, 30)
        val material = Material(
            assetManager,
            "Common/MatDefs/Misc/Particle.j3md"
        )
        fire.setMaterial(material)
        material.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"))
        fire.imagesX = 2
        fire.imagesY = 2 // 2x2 texture animation
        fire.endColor = ColorRGBA(1f, 0f, 0f, 1f) // red
        fire.startColor = ColorRGBA(1f, 1f, 0f, 0.5f) // yellow
        fire.particleInfluencer.initialVelocity = Vector3f(0f, 2f, 0f)
        fire.startSize = 0.5f
        fire.endSize = 0.1f
        fire.setGravity(0f, 0f, 0f)
        fire.lowLife = 1f
        fire.highLife = 3f
        fire.particleInfluencer.velocityVariation = 0.3f
        attachChild(fire)
    }

}