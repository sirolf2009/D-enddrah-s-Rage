package com.dendrrahsrage.control

import com.jme3.anim.AnimComposer
import com.jme3.bullet.control.BetterCharacterControl
import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import com.jme3.scene.Node

class BetterPlayerControl(
    private val characterNode: Node,
    private val camera: Camera,
    private val animationComposer: AnimComposer
) : BetterCharacterControl(0.3f, 2.5f, 8f) {

    var leftStrafe = false
    var rightStrafe = false
    var forward = false
    var backward = false
    var leftRotate = false
    var rightRotate = false

    override fun update(tpf: Float) {
        super.update(tpf)

        // Get current forward and left vectors of model by using its rotation
        // to rotate the unit vectors
        val modelForwardDir: Vector3f = characterNode.getWorldRotation().mult(Vector3f.UNIT_Z)
        val modelLeftDir: Vector3f = characterNode.getWorldRotation().mult(Vector3f.UNIT_X)


        // WalkDirection is global!
        // You *can* make your character fly with this.
        walkDirection[0f, 0f] = 0f
        if (leftStrafe) {
            walkDirection.addLocal(modelLeftDir.mult(3f))
        } else if (rightStrafe) {
            walkDirection.addLocal(modelLeftDir.negate().multLocal(3f))
        }
        if (forward) {
            if(animationComposer.getCurrentAction("Default") == null) {
                animationComposer.setCurrentAction("walkForward")
            }
            walkDirection.addLocal(modelForwardDir.mult(3f))
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(3f))
        } else {
            animationComposer.removeCurrentAction()
        }
        setWalkDirection(walkDirection)


        // ViewDirection is local to characters physics system!
        // The final world rotation depends on the gravity and on the state of
        // setApplyPhysicsLocal()
        if (leftRotate) {
            val rotateL = Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y)
            rotateL.multLocal(viewDirection)
        } else if (rightRotate) {
            val rotateR = Quaternion().fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y)
            rotateR.multLocal(viewDirection)
        }
        setViewDirection(viewDirection)
//        fpsText.setText("Touch da ground = " + physicsCharacter.isOnGround())
//        if (!lockView) {
            camera.lookAt(characterNode.getWorldTranslation().add(Vector3f(0f, 2f, 0f)), Vector3f.UNIT_Y)
//        }
    }

}