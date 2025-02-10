package com.dendrrahsrage.control

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.util.Gizmo
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.math.FastMath
import com.jme3.math.Vector3f
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.Spatial
import com.jme3.scene.control.AbstractControl
import kotlin.math.abs
import kotlin.math.atan2

class MoveToTarget(
    val target: Vector3f,
    val arrivalDistance: Float,
    val torque: Float = 1000f,
    val force: Float = 1f,
    val onArrived: (MoveToTarget) -> Unit = { it.removeFromSpatial() }
) : AbstractControl() {

    private var rigidBodyControl: RigidBodyControl? = null

    override fun controlUpdate(tpf: Float) {
        rigidBodyControl?.let { rigidBodyControl ->
            val distance = spatial.worldTranslation.distance(target)
            if(distance <= arrivalDistance) {
                onArrived.invoke(this)
            } else {
                val forward = spatial.localRotation.mult(Vector3f.UNIT_X).mult(force)
                rigidBodyControl.linearVelocity = forward

                val angleTowardsTarget = abs(
                    (atan2(
                        (target.z - spatial.worldTranslation.z).toDouble(),
                        (target.x - spatial.worldTranslation.x).toDouble()
                    ) * FastMath.RAD_TO_DEG)
                ) % 360
                val currentAngle = abs(rigidBodyControl.physicsRotation.toAngles(null)[1] * FastMath.RAD_TO_DEG) % 360
                val torque = if (currentAngle - angleTowardsTarget > 0) torque else -torque
                rigidBodyControl.applyTorque(Vector3f(0f, torque, 0f))
            }
        }
    }

    fun removeFromSpatial() {
        spatial.removeControl(this)
    }

    override fun setSpatial(spatial: Spatial?) {
        super.setSpatial(spatial)
        rigidBodyControl = spatial?.getControl(RigidBodyControl::class.java)
        addGizmo()
    }

    fun addGizmo() {
        Gizmo(DendrrahsRage.instance!!.assetManager).apply {
            setLocalTranslation(target)
            DendrrahsRage.instance!!.rootNode.attachChild(
                this
            )
        }
    }

    override fun controlRender(rm: RenderManager?, vp: ViewPort?) {
    }
}
