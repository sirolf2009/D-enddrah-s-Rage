package com.dendrrahsrage.control

import com.jme3.bullet.control.RigidBodyControl
import com.jme3.math.Vector3f
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.Spatial
import com.jme3.scene.control.AbstractControl
import com.jme3.terrain.geomipmap.TerrainQuad

class MoveToTarget(
    val target: Vector3f,
    val terrainQuad: TerrainQuad,
    val arrivalDistance: Float,
    val onArrived: () -> Unit = {}
) : AbstractControl() {

    private lateinit var rigidBodyControl: RigidBodyControl

    override fun controlUpdate(tpf: Float) {
    }

    override fun setSpatial(spatial: Spatial?) {
        super.setSpatial(spatial)
        rigidBodyControl = spatial!!.getControl(RigidBodyControl::class.java)
    }

    override fun controlRender(rm: RenderManager?, vp: ViewPort?) {
    }
}
