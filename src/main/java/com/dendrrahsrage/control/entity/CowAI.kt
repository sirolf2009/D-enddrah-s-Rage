package com.dendrrahsrage.control.entity

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.control.MoveToTarget
import com.dendrrahsrage.util.Gizmo
import com.jme3.anim.AnimComposer
import com.jme3.anim.tween.action.ClipAction
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import com.jme3.scene.control.AbstractControl
import com.jme3.terrain.geomipmap.TerrainQuad
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.random.Random

class CowAI(val terrain: TerrainQuad) : AbstractControl() {

    var rigidBodyControl: RigidBodyControl? = null
    var animComposer: AnimComposer? = null
    var moveToTarget: MoveToTarget? = null

    override fun controlUpdate(tpf: Float) {
        if(moveToTarget == null) {
            animComposer?.removeAction("Walk")
            if(Random.nextInt(500) <= tpf) {
                val point = getRandomTarget()
                moveToTarget = MoveToTarget(
                    target = point,
                    torque = 5000f,
                    arrivalDistance = 3f,
                    onArrived = {
                        spatial.removeControl(moveToTarget)
                    }
                )
                spatial.addControl(moveToTarget)
            }
        } else {
            if((animComposer?.getCurrentAction("Default") as? ClipAction)?.animClip?.name != "Walk") {
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
        val range = 10
        val x = spatial.worldTranslation.x + (Random.nextInt(range) - range/2f)
        val y = spatial.worldTranslation.y + (Random.nextInt(range) - range/2f)
        val height = terrain.getHeight(Vector2f(x, y))
        return Vector3f(x, height, y)
    }

    override fun controlRender(p0: RenderManager?, p1: ViewPort?) {

    }
}
