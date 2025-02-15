package com.dendrrahsrage.entity

import com.dendrrahsrage.World
import com.dendrrahsrage.control.player.BetterPlayerControl
import com.dendrrahsrage.control.CurseControl
import com.dendrrahsrage.item.Items
import com.jme3.anim.AnimComposer
import com.jme3.anim.ArmatureMask
import com.jme3.anim.SkinningControl
import com.jme3.anim.tween.action.ClipAction
import com.jme3.asset.AssetManager
import com.jme3.bullet.PhysicsSpace
import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import com.jme3.scene.CameraNode
import com.jme3.scene.Node
import com.jme3.scene.control.CameraControl.ControlDirection
import com.jme3.terrain.geomipmap.TerrainQuad

class EntityPlayer(
    terrain: TerrainQuad,
    val assetManager: AssetManager,
    camera: Camera,
): AbstractEntity("Player", terrain) {

    val model: Node
    val animComposer: AnimComposer
    val camNode: CameraNode
    val betterPlayerControl: BetterPlayerControl
    val skinningControl: SkinningControl

    var world: World? = null

    init {
        model = assetManager.loadModel("Models/character.glb") as Node
        attachChild(model)
        animComposer = model.getChild(0).getControl(AnimComposer::class.java)
        animComposer.addAction("walkForward", ClipAction(animComposer.getAnimClip("walkForward")))
        animComposer.addAction("idle", ClipAction(animComposer.getAnimClip("idle")))
        animComposer.setCurrentAction("idle")
        skinningControl = model.getChild(0).getControl(SkinningControl::class.java)

        camera.setLocation(Vector3f(10f, 6f, -5f))
        camNode = CameraNode(CameraNodeName, camera)
        camNode.setControlDir(ControlDirection.SpatialToCamera)
        camNode.setLocalTranslation(Vector3f(0f, 2f, -6f))
        attachChild(camNode)

        addControl(CurseControl())

        betterPlayerControl = BetterPlayerControl(this)
        addControl(betterPlayerControl)

        val attackMask = ArmatureMask(skinningControl.armature)
        attackMask.removeJoints(skinningControl.armature, "mixamorig1:LeftUpLeg", "mixamorig1:RightUpLeg")
        animComposer.makeLayer("attack", attackMask)

        betterPlayerControl.inventory.addItem(Items.Burger(assetManager))
        betterPlayerControl.inventory.addItem(Items.Leek(assetManager))
        betterPlayerControl.inventory.addItem(Items.Cake(assetManager))
        betterPlayerControl.inventory.addItem(Items.Log(assetManager))
        betterPlayerControl.inventory.addItem(Items.Log(assetManager))
        betterPlayerControl.inventory.addItem(Items.Log(assetManager))
        betterPlayerControl.inventory.addItem(Items.Log(assetManager))
        betterPlayerControl.inventory.addItem(Items.Log(assetManager))
        betterPlayerControl.inventory.addItem(Items.Log(assetManager))
        betterPlayerControl.inventory.addItem(Items.Log(assetManager))
        betterPlayerControl.inventory.addItem(Items.Log(assetManager))
        betterPlayerControl.inventory.addItem(Items.Campfire(assetManager))
        betterPlayerControl.inventory.addItem(Items.GreatSword(assetManager))
    }

    override fun getRigidBody() = betterPlayerControl.getRigidBody()

    companion object {

        val CameraNodeName = "CamNode"

    }

}
