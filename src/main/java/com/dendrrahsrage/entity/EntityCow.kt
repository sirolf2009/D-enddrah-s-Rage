package com.dendrrahsrage.entity

import com.dendrrahsrage.control.HealthControl
import com.dendrrahsrage.control.entity.CowAI
import com.jme3.anim.AnimComposer
import com.jme3.anim.tween.action.ClipAction
import com.jme3.asset.AssetManager
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.jme3.terrain.geomipmap.TerrainQuad

class EntityCow(
    terrain: TerrainQuad,
    val assetManager: AssetManager,
) : AbstractEntity("Cow" ,terrain) {

    val cowModel: Node
    val animComposer: AnimComposer
    val rigidBodyControl: RigidBodyControl
    val healthControl: HealthControl
    val cowAI: CowAI

    init {
        cowModel = assetManager.loadModel("Models/cow.glb") as Node
        animComposer = cowModel.getChild(0).getControl(AnimComposer::class.java)
        animComposer.addAction("Walk", ClipAction(animComposer.getAnimClip("Walk")))
        attachChild(cowModel)
        rigidBodyControl = RigidBodyControl(1000f).also { addControl(it) }
        healthControl = HealthControl(assetManager).also { addControl(it) }
        cowAI = CowAI(terrain).also { addControl(it) }
        val cowX = 201f
        val cowY = 200f
        setLocalTranslation(cowX, terrain.getHeight(Vector2f(cowX, cowY)) + 1, cowY)
        rigidBodyControl.physicsLocation = Vector3f(cowX, terrain.getHeight(Vector2f(cowX, cowY)) + 1, cowY)
        rigidBodyControl.friction = 4f
    }

    override fun getRigidBody() = rigidBodyControl

}
