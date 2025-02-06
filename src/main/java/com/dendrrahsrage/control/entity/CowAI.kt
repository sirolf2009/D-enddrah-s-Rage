package com.dendrrahsrage.control.entity

import com.dendrrahsrage.DendrrahsRage
import com.jme3.anim.AnimComposer
import com.jme3.bullet.collision.shapes.CollisionShape
import com.jme3.bullet.control.BetterCharacterControl
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.bullet.util.CollisionShapeFactory
import com.jme3.math.Quaternion
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import com.jme3.scene.control.AbstractControl
import com.jme3.terrain.geomipmap.TerrainQuad
import kotlin.random.Random

class CowAI(val terrain: TerrainQuad) : AbstractControl() {

    var rigidBodyControl: RigidBodyControl? = null
    var animComposer: AnimComposer? = null
    var target: Vector3f? = null

    override fun controlUpdate(tpf: Float) {
        if(target == null) {
//            if(Random.nextInt(1000) <= tpf) {
                target = getRandomTarget()
//            }
        } else {
            val distance = spatial.worldTranslation.distance(target)
            if(distance < 3) {
                target = null
            } else {
                val direction = target!!.subtract(spatial.worldTranslation).mult(1f, 0f, 1f).normalize()
                rigidBodyControl!!.linearVelocity = direction

                val rotation = Quaternion()
                rotation.lookAt(direction, Vector3f.UNIT_Y)
                rigidBodyControl!!.applyTorque(rotation.mult(Vector3f(1f,1f,1f)))
                //spatial.lookAt(target, Vector3f.UNIT_Y)
                animComposer!!.setCurrentAction("Walk")
            }
        }
    }

    override fun setSpatial(spatial: Spatial?) {
        super.setSpatial(spatial)
        rigidBodyControl = spatial!!.getControl(RigidBodyControl::class.java)
        animComposer = ((spatial as Node).getChild(0) as Node).getChild(0).getControl(AnimComposer::class.java)
    }

    fun getRandomTarget(): Vector3f {
        val range = 100
        val x = spatial.worldTranslation.x + (Random.nextInt(range) - range/2f)
        val y = spatial.worldTranslation.y + (Random.nextInt(range) - range/2f)
        val height = terrain.getHeight(Vector2f(x, y))
        return Vector3f(x, height, y)
    }

    override fun controlRender(p0: RenderManager?, p1: ViewPort?) {

    }
}
