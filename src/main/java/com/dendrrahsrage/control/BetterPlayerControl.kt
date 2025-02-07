package com.dendrrahsrage.control

import com.dendrrahsrage.item.Inventory
import com.dendrrahsrage.item.WeaponItem
import com.jme3.anim.AnimComposer
import com.jme3.anim.SkinningControl
import com.jme3.anim.tween.action.Action
import com.jme3.anim.tween.action.ClipAction
import com.jme3.bullet.control.BetterCharacterControl
import com.jme3.collision.CollisionResults
import com.jme3.math.Vector3f
import com.jme3.scene.CameraNode
import com.jme3.scene.Node
import com.jme3.terrain.geomipmap.TerrainQuad

class BetterPlayerControl(
    val characterNode: Node,
    val camera: CameraNode,
    private val animationComposer: AnimComposer
) : BetterCharacterControl(0.3f, 1.9f, 80f) {

    var leftStrafe = false
    var rightStrafe = false
    var forward = false
    var backward = false
    var leftRotate = false
    var rightRotate = false
    var isAttacking = false

    var health = 100.0
    var hunger = 70.0

    val inventory = Inventory()
    private var equipedItem: WeaponItem? = null

    override fun update(tpf: Float) {
        super.update(tpf)

        // Get current forward and left vectors of model by using its rotation
        // to rotate the unit vectors
        val modelForwardDir: Vector3f = characterNode.worldRotation.mult(Vector3f.UNIT_Z)
        val modelLeftDir: Vector3f = characterNode.worldRotation.mult(Vector3f.UNIT_X)

        // WalkDirection is global!
        // You *can* make your character fly with this.
        walkDirection[0f, 0f] = 0f
        if (leftStrafe) {
            walkDirection.addLocal(modelLeftDir.mult(3f))
        } else if (rightStrafe) {
            walkDirection.addLocal(modelLeftDir.negate().multLocal(3f))
        }
        if (forward) {
            setAnimation(equipedItem?.getWalkForward() ?: "walkForward")
            walkDirection.addLocal(modelForwardDir.mult(6f))
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(3f))
        } else {
            setAnimation(equipedItem?.getIdle() ?: "idle")
        }
        setWalkDirection(walkDirection)

        camera.lookAt(characterNode.getWorldTranslation().add(Vector3f(0f, 2f, 0f)), Vector3f.UNIT_Y)

        hunger -= tpf / 100

//        if(isAttacking) {
//            val collisionResults = CollisionResults()
//            physicsSpace.
//        }
    }

    fun attack() {
        isAttacking = true
        setAnimation(equipedItem?.getAttack() ?: "attack", "attack", false)
        physicsSpace.addCollisionListener { evt ->
            if(evt.nodeA == characterNode) {
                println("it's a me, mario")
                if(evt.nodeB !is TerrainQuad) {
                    val collisionResults = CollisionResults()
                    equipedItem!!.model.collideWith(evt.nodeB, collisionResults)
                    println(collisionResults.size())
                    println(collisionResults)
                }
            }
            if(evt.nodeB == characterNode) {
                println("it's a me, mario b, I collided with ${evt.nodeA}")
                if(evt.nodeA !is TerrainQuad) {
                    val collisionResults = CollisionResults()
                    equipedItem!!.model.collideWith(evt.nodeA, collisionResults)
                    println(collisionResults.size())
                    println(collisionResults)
                }
            }
        }
    }

    fun setAnimation(name: String, layer: String = "Default", loop: Boolean = true): ClipAction {
        val action = animationComposer.getCurrentAction(layer) as? ClipAction
        if (action == null || !action.animClip.name.equals(name)) {
            return animationComposer.setCurrentAction(name, layer, loop) as ClipAction
        }
        return action
    }

    fun equip(item: WeaponItem) {
        unequip()
        equipedItem = item
        getRightHand().attachChild(item.model)
        item.model.scale(50f)
        item.onEquipped()
    }

    fun unequip() {
        equipedItem?.let {
            it.model.scale(1 / 50f)
            inventory.addItem(it)
            getRightHand().detachChild(it.model)
        }
    }

    fun getRightHand() = getSkinningControl().getAttachmentsNode("mixamorig1:RightHand")

    fun getSkinningControl() =
        ((characterNode.getChild("Scene") as Node).getChild(0) as Node).getControl(SkinningControl::class.java)

}