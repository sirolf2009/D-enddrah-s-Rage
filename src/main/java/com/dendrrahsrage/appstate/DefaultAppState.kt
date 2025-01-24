/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.appstate

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.actionlistener.BetterWASDMovement
import com.dendrrahsrage.actionlistener.WASDMovement
import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.control.PlayerControl
import com.dendrrahsrage.gui.hud.HUD
import com.jme3.anim.AnimComposer
import com.jme3.anim.tween.action.ClipAction
import com.jme3.app.Application
import com.jme3.app.state.AppStateManager
import com.jme3.asset.plugins.HttpZipLocator
import com.jme3.bullet.BulletAppState
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.bullet.util.CollisionShapeFactory
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseButtonTrigger
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.CameraNode
import com.jme3.scene.Node
import com.jme3.scene.control.CameraControl.ControlDirection

class DefaultAppState(private val application: DendrrahsRage) : BulletAppState() {
    private val stateNode: Node
    private var player: PlayerControl? = null
    private var betterPlayerControl: BetterPlayerControl? = null

    init {
        stateNode = Node("Default state")
    }

    override fun initialize(stateManager: AppStateManager, app: Application) {
        super.initialize(stateManager, app)
    }

    override fun stateAttached(stateManager: AppStateManager) {
        super.stateAttached(stateManager) // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        application.viewPort.backgroundColor = ColorRGBA(0.7f, 0.8f, 1f, 1f)
        setUpLight()

        // We load the scene from the zip file and adjust its size.
        application.assetManager.registerLocator(
            "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/town.zip",
            HttpZipLocator::class.java
        )
        val sceneModel = application.assetManager.loadModel("main.scene")
        sceneModel.setLocalScale(2f)

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        val sceneShape = CollisionShapeFactory.createMeshShape(sceneModel)
        val landscape = RigidBodyControl(sceneShape, 0f)
        sceneModel.addControl(landscape)

        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        application.getRootNode().attachChild(sceneModel)

        physicsSpace.add(landscape)

        setupBetterPlayer()

        isDebugEnabled = true

        HUD(application, application.getGuiNode())
    }

    fun setupBetterPlayer() {
        val characterNode = Node("character node")
        characterNode.setLocalTranslation(0f, 2f, 0f)

        val model = application.assetManager.loadModel("Models/character.glb") as Node
        characterNode.attachChild(model)
        val animComposer = model.getChild(0).getControl(AnimComposer::class.java)
        animComposer.addAction("walkForward", ClipAction(animComposer.getAnimClip("walkForward")))
        animComposer.setCurrentAction("walkForward")


        application.camera.setLocation(Vector3f(10f, 6f, -5f))
        val camNode = CameraNode("CamNode", application.camera)
        camNode.setControlDir(ControlDirection.SpatialToCamera)
        camNode.setLocalTranslation(Vector3f(0f, 2f, -6f))

        betterPlayerControl = BetterPlayerControl(characterNode, camNode, animComposer)
        characterNode.addControl(betterPlayerControl)
        physicsSpace.add(betterPlayerControl)

        val quat = Quaternion()

        // These coordinates are local, the camNode is attached to the character node!
        quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y)
        camNode.localRotation = quat
        characterNode.attachChild(camNode)

        // Disable by default, can be enabled via keyboard shortcut
        application.flyByCamera.isEnabled = false
        camNode.isEnabled = true
        application.inputManager.isCursorVisible = false

        BetterWASDMovement(betterPlayerControl!!).setupKeys(application.inputManager)
        application.rootNode.attachChild(characterNode)
    }

    fun setupPlayer() {
        val characterNode = Node()
        val character = application.assetManager.loadModel("Models/character.glb") as Node
        character.setLocalTranslation(0f, -0.6f, 0f)
        character.setLocalScale(5F)
        application.getRootNode().attachChild(characterNode)
        val animComposer = character.getChild(0).getControl(AnimComposer::class.java)
        animComposer.addAction("walkForward", ClipAction(animComposer.getAnimClip("walkForward")))
        animComposer.setCurrentAction("walkForward")
        characterNode.attachChild(character)

        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, step height, jumping, falling, and gravity.
        // We also put the player in its starting position.
        val capsuleShape = CapsuleCollisionShape(1.5f, 1f, 1)
        player = PlayerControl(this.application, capsuleShape, character)
        characterNode.addControl(player)
        physicsSpace.add(player)

        setUpKeys()
    }

    private fun setUpLight() {
        // We add light so we see the scene
        val al = AmbientLight()
        al.color = ColorRGBA.White.mult(1.3f)
        application.getRootNode().addLight(al)

        val dl = DirectionalLight()
        dl.color = ColorRGBA.White
        dl.direction = Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal()
        application.getRootNode().addLight(dl)
    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private fun setUpKeys() {
        val inputManager = application.inputManager
        val wasdMovement = player?.let { WASDMovement(it) }
        inputManager.addMapping("Left", KeyTrigger(KeyInput.KEY_A))
        inputManager.addMapping("Right", KeyTrigger(KeyInput.KEY_D))
        inputManager.addMapping("Up", KeyTrigger(KeyInput.KEY_W))
        inputManager.addMapping("Down", KeyTrigger(KeyInput.KEY_S))
        inputManager.addMapping("Jump", KeyTrigger(KeyInput.KEY_SPACE))
        inputManager.addMapping(
            "shoot",
            MouseButtonTrigger(MouseInput.BUTTON_LEFT)
        )
        inputManager.addListener(wasdMovement, "Left", "Right", "Up", "Down", "Jump")
    }

    override fun cleanup() {
        super.cleanup()
        application.getRootNode().detachChild(stateNode)
    }

    override fun update(tpf: Float) {
        super.update(tpf)
//        player!!.update(tpf)
        betterPlayerControl?.update(tpf)

        application.inputManager.isCursorVisible = false
    }
}
