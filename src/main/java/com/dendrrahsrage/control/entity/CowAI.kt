package com.dendrrahsrage.control.entity

import com.dendrrahsrage.DendrrahsRage
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
    var target: Vector3f? = null

    override fun controlUpdate(tpf: Float) {
        if(target == null) {
//            if(Random.nextInt(1000) <= tpf) {
                target = getRandomTarget()
            Gizmo(DendrrahsRage.instance!!.assetManager).apply {
                setLocalTranslation(target)
                DendrrahsRage.instance!!.rootNode.attachChild(
                    this
                )
            }
//            }
        } else {
            val distance = spatial.worldTranslation.distance(target)
            if(distance < 3) {
                target = null
            } else {
                val direction = target!!.subtract(spatial.worldTranslation).mult(1f, 0f, 1f).normalize()
                val waypoint = spatial.worldTranslation.add(direction)
                val height = terrain.getHeight(Vector2f(waypoint.x, waypoint.z))
                val stepHeight = height - spatial.worldTranslation.y
                direction.addLocal(0f, stepHeight, 0f)
                rigidBodyControl!!.linearVelocity = direction


//                val objectPosition = rigidBodyControl!!.physicsLocation
//                // Calculate the direction vector from the object to the target
//                val directionToTarget: Vector3f = waypoint.subtract(objectPosition).normalize()
//                // Calculate the desired rotation (target direction) using quaternion
//                val targetRotation = Quaternion()
//                targetRotation.lookAt(waypoint, Vector3f.UNIT_Y)
//
//
//                // Calculate the difference between the current and target rotation (error)
//                val deltaRotation = targetRotation.inverse().mult(rigidBodyControl!!.physicsRotation)
//
//
//                // Convert the delta rotation to a torque vector
//                val torque: Vector3f = deltaRotation.multLocal(Vector3f(0f, 5000f, 0f))
//                println(torque)
//
//
//                // Apply torque to the rigid body control
//                rigidBodyControl!!.setPhysicsRotation(targetRotation)
////                rigidBodyControl!!.applyTorque(torque)


                val angleTowardsTarget = abs((atan2(
                    (target!!.z - spatial.worldTranslation.z).toDouble(),
                    (target!!.x - spatial.worldTranslation.x).toDouble()
                ) * FastMath.RAD_TO_DEG)) % 360
                val currentAngle = abs(rigidBodyControl!!.physicsRotation.toAngles(null).get(1) * FastMath.RAD_TO_DEG) % 360
                val torque = if (currentAngle - angleTowardsTarget > 0) 1000f else -1000f
                println("${((currentAngle - angleTowardsTarget)).toInt()} ${currentAngle.toInt()} ${angleTowardsTarget.toInt()}")
                rigidBodyControl!!.applyTorque(Vector3f(0f, torque, 0f))
                if((animComposer?.getCurrentAction("Default") as? ClipAction)?.animClip?.name != "Walk") {
                    animComposer!!.setCurrentAction("Walk")
                }
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
