package com.dendrrahsrage.control

import com.dendrrahsrage.Player
import com.dendrrahsrage.item.Inventory
import com.dendrrahsrage.item.WeaponItem
import com.jme3.anim.AnimComposer
import com.jme3.anim.SkinningControl
import com.jme3.anim.tween.action.Action
import com.jme3.anim.tween.action.ClipAction
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape
import com.jme3.bullet.control.BetterCharacterControl
import com.jme3.bullet.control.GhostControl
import com.jme3.collision.CollisionResults
import com.jme3.math.Vector3f
import com.jme3.scene.CameraNode
import com.jme3.scene.Node
import com.jme3.terrain.geomipmap.TerrainQuad

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
    private var equipedItem: EquipmentNode? = null
    private var attack: Attack? = null

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
            setAnimation(equipedItem?.item?.getWalkForward() ?: "walkForward")
            walkDirection.addLocal(modelForwardDir.mult(6f))
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.negate().multLocal(3f))
        } else {
            setAnimation(equipedItem?.item?.getIdle() ?: "idle")
        }
        setWalkDirection(walkDirection)

        player.getCameraNode().lookAt(player.node.getWorldTranslation().add(Vector3f(0f, 2f, 0f)), Vector3f.UNIT_Y)

        hunger -= tpf / 100

        attack?.update()
    }

    fun attack() {
        if(attack == null) {
            setAnimation(equipedItem?.item?.getAttack() ?: "attack", "attack", false)
            attack = Attack(player)
        }
    }

    fun setAnimation(name: String, layer: String = "Default", loop: Boolean = true): ClipAction {
        val action = player.getAnimComposer().getCurrentAction(layer) as? ClipAction
        if (action == null || !action.animClip.name.equals(name)) {
            return player.getAnimComposer().setCurrentAction(name, layer, loop) as ClipAction
        }
        return action
    }

    fun equip(item: WeaponItem) {
        unequip()

        item.model.scale(50f)

        val equipmentCollisionNode = item.createCollisionShape()

        val equipmentNode = EquipmentNode(
            "rightHandEquipment",
            item,
            item.model,
            equipmentCollisionNode
        )
        equipedItem = equipmentNode
        getRightHand().attachChild(equipmentNode)
        physicsSpace.add(equipmentCollisionNode)
        item.onEquipped()
    }

    fun unequip() {
        equipedItem?.let {
            it.model.scale(1 / 50f)
            it.collision.removeControl(GhostControl::class.java)
            inventory.addItem(it.item)
            getRightHand().detachChild(it)
        }
    }

    fun getRightHand() = player.getSkinningControl().getAttachmentsNode("mixamorig1:RightHand")

    fun getRigidBody() = rigidBody

    class Attack(
        val player: Player,
        val ghostControl: GhostControl = player.getPlayerControl().equipedItem!!.collision.getControl(GhostControl::class.java)
    ) {

        val targetsHit = mutableListOf<Node>()

        fun update() {
            ghostControl.overlappingObjects.filter {
                it.userObject is Node && !targetsHit.contains(it.userObject) && it.userObject != player.node
            }.forEach {
                val node = it.userObject as Node
                targetsHit.add(node)
                node.getControl(HealthControl::class.java)?.let { health ->
                    println("hit "+node)
                    health.damage(player.getPlayerControl().equipedItem!!.item.getDamage())
                }
            }
            if(hasAnimationFinished()) {
                player.getPlayerControl().attack = null
            }
        }

        fun hasAnimationFinished(): Boolean = player.getAnimComposer().getCurrentAction("attack") == null

    }

    data class EquipmentNode(
        private val name: String,
        val item: WeaponItem,
        val model: Node,
        val collision: Node
    ): Node(name) {

        init {
            attachChild(model)
            attachChild(collision)
        }

    }

}
