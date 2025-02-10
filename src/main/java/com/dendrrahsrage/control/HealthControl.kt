package com.dendrrahsrage.control

import com.dendrrahsrage.item.Items
import com.jme3.asset.AssetManager
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.Node
import com.jme3.scene.control.AbstractControl

class HealthControl(
    val assetManager: AssetManager,
): AbstractControl() {

    private var health = 100f

    override fun controlUpdate(p0: Float) {
    }

    override fun controlRender(p0: RenderManager?, p1: ViewPort?) {
    }

    fun damage(damage: Float) {
        this.health -= damage
        if(this.health <= 0f) {
            println("dead")
            val owner = this.spatial as Node
            val ownerRigidBodyControl = owner.getControl(RigidBodyControl::class.java)
            val item = Items.MeatRaw(assetManager).spawnItem(
                owner.parent,
                ownerRigidBodyControl.physicsSpace
            )
            item.localTranslation = owner.localTranslation
            item.getControl(RigidBodyControl::class.java).physicsLocation = ownerRigidBodyControl.physicsLocation
            owner.parent.detachChild(owner)
            ownerRigidBodyControl.physicsSpace.add(item)
            ownerRigidBodyControl.physicsSpace.remove(owner)
        }
    }
}
