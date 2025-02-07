package com.dendrrahsrage.control

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.util.Gizmo
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.math.FastMath
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.Spatial
import com.jme3.scene.control.AbstractControl
import com.jme3.terrain.geomipmap.TerrainQuad
import kotlin.math.abs
import kotlin.math.atan2

class MoveToTarget(
    val target: Vector3f,
    val terrain: TerrainQuad,
    val arrivalDistance: Float,
    val onArrived: (MoveToTarget) -> Unit = { it.spatial.removeControl(it) }
) : AbstractControl() {

    private var rigidBodyControl: RigidBodyControl? = null

    override fun controlUpdate(tpf: Float) {
        rigidBodyControl?.let { rigidBodyControl ->
            val distance = spatial.worldTranslation.distance(target)
            if(distance <= arrivalDistance) {
                onArrived.invoke(this)
            } else {
                val direction = target.subtract(spatial.worldTranslation).mult(1f, 0f, 1f).normalize()
                val waypoint = spatial.worldTranslation.add(direction)
                val height = terrain.getHeight(Vector2f(waypoint.x, waypoint.z))
                val stepHeight = height - spatial.worldTranslation.y
                direction.addLocal(0f, stepHeight, 0f)
                rigidBodyControl.linearVelocity = direction

                val angleTowardsTarget = abs(
                    (atan2(
                        (target.z - spatial.worldTranslation.z).toDouble(),
                        (target.x - spatial.worldTranslation.x).toDouble()
                    ) * FastMath.RAD_TO_DEG)
                ) % 360
                val currentAngle = abs(rigidBodyControl.physicsRotation.toAngles(null)[1] * FastMath.RAD_TO_DEG) % 360
                val torque = if (currentAngle - angleTowardsTarget > 0) 1000f else -1000f
                println("${((currentAngle - angleTowardsTarget)).toInt()} ${currentAngle.toInt()} ${angleTowardsTarget.toInt()}")
                rigidBodyControl.applyTorque(Vector3f(0f, torque, 0f))
            }
        }
    }

    override fun setSpatial(spatial: Spatial?) {
        super.setSpatial(spatial)
        rigidBodyControl = spatial?.getControl(RigidBodyControl::class.java)
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
