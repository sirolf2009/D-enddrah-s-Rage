package com.dendrrahsrage.control

import com.dendrrahsrage.Player
import com.dendrrahsrage.item.Inventory
import com.jme3.anim.AnimComposer
import com.jme3.anim.tween.action.ClipAction
import com.jme3.bullet.control.BetterCharacterControl
import com.jme3.math.Vector3f
import com.jme3.scene.CameraNode
import com.jme3.scene.Node

class BetterPlayerControl(
    val player: Player
) : BetterCharacterControl(0.3f, 1.9f, 80f) {

    var leftStrafe = false
    var rightStrafe = false
    var forward = false
    var backward = false
    var leftRotate = false
    var rightRotate = false

    var health = 100.0
    var hunger = 70.0

    val inventory = Inventory()

    override fun update(tpf: Float) {
        super.update(tpf)

        // Get current forward and left vectors of model by using its rotation
        // to rotate the unit vectors
        val modelForwardDir: Vector3f = player.node.worldRotation.mult(Vector3f.UNIT_Z)
        val modelLeftDir: Vector3f = player.node.worldRotation.mult(Vector3f.UNIT_X)

        // WalkDirection is global!
        // You *can* make your character fly with this.
        walkDirection[0f, 0f] = 0f
        if (leftStrafe) {
            walkDirection.addLocal(modelLeftDir.mult(3f))
        } else if (rightStrafe) {
            walkDirection.addLocal(modelLeftDir.negate().multLocal(3f))
        }
        if (forward) {
            setAnimation("walkForward")
            walkDirection.addLocal(modelForwardDir.mult(6f))
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(3f))
        } else {
            setAnimation("idle")
        }
        setWalkDirection(walkDirection)

        player.getCameraNode().lookAt(player.node.getWorldTranslation().add(Vector3f(0f, 2f, 0f)), Vector3f.UNIT_Y)

        hunger -= tpf / 100
    }

    fun setAnimation(name: String) {
        val action = player.getAnimComposer().getCurrentAction("Default") as ClipAction
        if(!action.animClip.name.equals(name)) {
            player.getAnimComposer().setCurrentAction(name)
        }
    }

    fun getRigidBody() = rigidBody

}