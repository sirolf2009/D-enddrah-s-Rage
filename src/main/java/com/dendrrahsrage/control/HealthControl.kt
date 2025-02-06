package com.dendrrahsrage.control

import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.control.AbstractControl

class HealthControl : AbstractControl() {

    private var health = 100f

    override fun controlUpdate(p0: Float) {
    }

    override fun controlRender(p0: RenderManager?, p1: ViewPort?) {
    }

    fun damage(damage: Float) {
        this.health -= damage
        if(this.health <= 0f) {
            println("dead")
        }
    }
}
