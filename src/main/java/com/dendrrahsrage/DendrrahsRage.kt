package com.dendrrahsrage

import com.dendrrahsrage.appstate.DefaultAppState
import com.dendrrahsrage.appstate.PlayerMovementAppState
import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.control.CurseControl
import com.dendrrahsrage.item.Items
import com.jme3.anim.AnimComposer
import com.jme3.anim.ArmatureMask
import com.jme3.anim.tween.action.ClipAction
import com.jme3.app.SimpleApplication
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.renderer.RenderManager
import com.jme3.scene.CameraNode
import com.jme3.scene.Node
import com.jme3.scene.control.CameraControl.ControlDirection
import com.jme3.system.AppSettings
import com.simsilica.lemur.GuiGlobals
import com.simsilica.lemur.style.BaseStyles


/**
 * This is the Main Class of your Game. It should boot up your game and do initial initialisation
 * Move your Logic into AppStates or Controls or other java classes
 */
class DendrrahsRage : SimpleApplication() {

    lateinit var player: Player
    var mouseCapture = true

    override fun simpleInitApp() {
        GuiGlobals.initialize(this)
        GuiGlobals.getInstance().isCursorEventsEnabled = false
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass")

        createPlayer()

        getStateManager().attach(DefaultAppState(this))
        getStateManager().attach(PlayerMovementAppState(this))
    }

    override fun simpleUpdate(tpf: Float) {
        inputManager.isCursorVisible = !mouseCapture
    }

    override fun simpleRender(rm: RenderManager) {
        //add render code here (if any)
    }

    fun getSettings() = settings

    fun createPlayer() {
        val characterNode = Node("character node")

        val model = assetManager.loadModel("Models/character.glb") as Node
        characterNode.attachChild(model)
        val animComposer = model.getChild(0).getControl(AnimComposer::class.java)
        animComposer.addAction("walkForward", ClipAction(animComposer.getAnimClip("walkForward")))
        animComposer.addAction("idle", ClipAction(animComposer.getAnimClip("idle")))
        animComposer.setCurrentAction("idle")

        camera.setLocation(Vector3f(10f, 6f, -5f))
        val camNode = CameraNode(Player.CameraNodeName, camera)
        camNode.setControlDir(ControlDirection.SpatialToCamera)
        camNode.setLocalTranslation(Vector3f(0f, 2f, -6f))
        characterNode.attachChild(camNode)

        characterNode.addControl(CurseControl())

        player = Player(characterNode)

        val betterPlayerControl = BetterPlayerControl(player)
        characterNode.addControl(betterPlayerControl)

        val attackMask = ArmatureMask(player.getSkinningControl().armature)
        attackMask.removeJoints(player.getSkinningControl().armature, "mixamorig1:LeftUpLeg", "mixamorig1:RightUpLeg")
        animComposer.makeLayer("attack", attackMask)

        betterPlayerControl.inventory.addItem(Items.Burger(assetManager))
        betterPlayerControl.inventory.addItem(Items.Leek(assetManager))
        betterPlayerControl.inventory.addItem(Items.Leek(assetManager))
        betterPlayerControl.inventory.addItem(Items.Leek(assetManager))
        betterPlayerControl.inventory.addItem(Items.Cake(assetManager))
        betterPlayerControl.inventory.addItem(Items.GreatSword(assetManager))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = DendrrahsRage()
            app.isShowSettings = false
            val settings = AppSettings(true)
            settings.title = "D'endrrah's Rage"
            settings.setWindowSize(1024, 800)
            app.setSettings(settings)
            app.start()
        }
    }
}
