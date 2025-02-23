package com.dendrrahsrage.item

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape
import com.jme3.bullet.control.GhostControl
import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.jme3.texture.Texture


open class GreatSwordItem(
    name: String,
    model: Node,
    icon: Texture,
    weight: Float,
) : WeaponItem(name, model, icon, weight) {

    override fun onEquipped() {
        model.setLocalTranslation(00f, 13f, 5f)

        val yRotation = Quaternion()
        yRotation.fromAngleAxis(FastMath.PI * 32 / 180, Vector3f(0f, 1f, 0f))
        val zRotation = Quaternion()
        zRotation.fromAngleAxis(FastMath.PI * 32 / 180, Vector3f(0f, 0f, 1f))
        model.setLocalRotation(yRotation.mult(zRotation))
    }

    override fun createCollisionShape(): Node {
        val collisionNode = Node("$name collision")
        val ghostControl = GhostControl(CapsuleCollisionShape(0.1f, 1f, 0))
        collisionNode.addControl(ghostControl)

        val yRotation = Quaternion()
        yRotation.fromAngleAxis(FastMath.PI * 32 / 180, Vector3f(0f, 1f, 0f))
        val zRotation = Quaternion()
        zRotation.fromAngleAxis(FastMath.PI * 32 / 180, Vector3f(0f, 0f, 1f))
        collisionNode.setLocalRotation(yRotation.mult(zRotation))

        collisionNode.setLocalTranslation(50f, 50f, -30f)

        return collisionNode
    }

    override fun getWalkForward() = "walkForwardGreatSword"
    override fun getIdle() = "idleGreatSword"
    override fun getAttack() = "attackGreatSword"

}
