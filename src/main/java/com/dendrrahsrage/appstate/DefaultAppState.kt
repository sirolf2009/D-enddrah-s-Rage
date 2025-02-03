/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.appstate

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.actionlistener.BetterWASDMovement
import com.dendrrahsrage.actionlistener.WASDMovement
import com.dendrrahsrage.control.*
import com.dendrrahsrage.gui.hud.HUD
import com.dendrrahsrage.item.Item
import com.dendrrahsrage.item.Items
import com.dendrrahsrage.jnoiseterrain.JNoiseHeightMap
import com.jme3.anim.AnimComposer
import com.jme3.anim.tween.action.ClipAction
import com.jme3.app.Application
import com.jme3.app.state.AppStateManager
import com.jme3.bullet.BulletAppState
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.font.BitmapText
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseButtonTrigger
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Quaternion
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.scene.CameraNode
import com.jme3.scene.Node
import com.jme3.scene.control.CameraControl.ControlDirection
import com.jme3.system.AppSettings
import com.jme3.terrain.geomipmap.TerrainLodControl
import com.jme3.terrain.geomipmap.TerrainQuad
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator
import com.jme3.terrain.heightmap.AbstractHeightMap
import com.jme3.terrain.heightmap.HillHeightMap
import com.jme3.terrain.heightmap.ImageBasedHeightMap
import com.jme3.texture.Texture
import com.jme3.texture.Texture.WrapMode

class DefaultAppState(private val application: DendrrahsRage, private val settings: AppSettings) : BulletAppState() {
    private val stateNode: Node
    private var player: PlayerControl? = null
    private var betterPlayerControl: BetterPlayerControl? = null
    private var hud: HUD? = null
    var mouseCapture = true

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

        val mat_terrain = Material(
            application.assetManager,
            "Common/MatDefs/Terrain/Terrain.j3md"
        )


        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures)  */
        mat_terrain.setTexture(
            "Alpha", application.assetManager.loadTexture(
                "Textures/Terrain/splat/alphamap.png"
            )
        )


        /** 1.2) Add GRASS texture into the red layer (Tex1).  */
        val grass: Texture = application.assetManager.loadTexture(
            "Textures/Terrain/splat/grass.jpg"
        )
        grass.setWrap(WrapMode.Repeat)
        mat_terrain.setTexture("Tex1", grass)
        mat_terrain.setFloat("Tex1Scale", 64f)


        /** 1.3) Add DIRT texture into the green layer (Tex2)  */
        val dirt: Texture = application.assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg"
        )
        dirt.setWrap(WrapMode.Repeat)
        mat_terrain.setTexture("Tex2", dirt)
        mat_terrain.setFloat("Tex2Scale", 32f)


        /** 1.4) Add ROAD texture into the blue layer (Tex3)  */
        val rock: Texture = application.assetManager.loadTexture(
            "Textures/Terrain/splat/road.jpg"
        )
        rock.setWrap(WrapMode.Repeat)
        mat_terrain.setTexture("Tex3", rock)
        mat_terrain.setFloat("Tex3Scale", 128f)


        /* 2.a Create a custom height map from an image */
        var heightmap: AbstractHeightMap? = null
/*        val heightMapImage: Texture = application.assetManager.loadTexture(
            "Textures/Terrain/splat/mountains512.png"
        )
        heightmap = ImageBasedHeightMap(heightMapImage.getImage())
*/
        heightmap = JNoiseHeightMap()
        heightmap.load()


        /** 3. We have prepared material and heightmap.
         * Now we create the actual terrain:
         * 3.1) Create a TerrainQuad and name it "my terrain".
         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
         * 3.5) We supply the prepared heightmap itself.
         */
        val patchSize = 65
        val terrain: TerrainQuad = TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap())


        /** 4. We give the terrain its material, position & scale it, and attach it.  */
        terrain.setMaterial(mat_terrain)
        //terrain.setLocalTranslation(40f, -50f, 0f)
        terrain.setLocalScale(4f, 4f, 4f)

        terrain.addControl(RigidBodyControl(0f))
        application.rootNode.attachChild(terrain)
        physicsSpace.add(terrain)


        /** 5. The LOD (level of detail) depends on were the camera is:  */
        val control: TerrainLodControl = TerrainLodControl(terrain, application.camera)
        control.setLodCalculator(DistanceLodCalculator(patchSize, 2.7f)) // patch size, and a multiplier
        terrain.addControl(control)

        terrain.addControl(GrowPlantsControl(GrowPlantsControl.defaultPlants(application.assetManager), physicsSpace))

        setupBetterPlayer(terrain)

        hud = HUD(application, application.getGuiNode(), betterPlayerControl!!)

        val bread = Items.Burger(application.assetManager).spawnItem(application.rootNode, physicsSpace)
        bread.getControl(RigidBodyControl::class.java).physicsLocation = Vector3f(1f, 0f, 2f)

        val cake = Items.Cake(application.assetManager).spawnItem(application.rootNode, physicsSpace)
        cake.setLocalTranslation(20f, 0f, 10f)


    }

    fun setupBetterPlayer(terrainQuad: TerrainQuad) {
        val characterNode = Node("character node")
        val characterX = 200f
        val characterY = 200f
        characterNode.setLocalTranslation(characterX, terrainQuad.getHeight(Vector2f(characterX, characterY)), characterY)

        val model = application.assetManager.loadModel("Models/character.glb") as Node
        characterNode.attachChild(model)
        val animComposer = model.getChild(0).getControl(AnimComposer::class.java)
        animComposer.addAction("walkForward", ClipAction(animComposer.getAnimClip("walkForward")))
        animComposer.addAction("idle", ClipAction(animComposer.getAnimClip("idle")))
        animComposer.setCurrentAction("idle")


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

        BetterWASDMovement(betterPlayerControl!!, application.rootNode, application.guiNode, this, application.inputManager).setupKeys()
        application.rootNode.attachChild(characterNode)

        characterNode.addControl(CurseControl())

        betterPlayerControl!!.inventory.addItem(Items.Burger(application.assetManager))
        betterPlayerControl!!.inventory.addItem(Items.Leek(application.assetManager))
        betterPlayerControl!!.inventory.addItem(Items.Leek(application.assetManager))
        betterPlayerControl!!.inventory.addItem(Items.Leek(application.assetManager))
        betterPlayerControl!!.inventory.addItem(Items.Cake(application.assetManager))

        initCrossHairs()
    }

    private fun initCrossHairs() {
        application.setDisplayStatView(false)
        val guiFont = application.assetManager.loadFont("Interface/Fonts/Default.fnt")
        val ch: BitmapText = BitmapText(guiFont)
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2f)
        ch.setText("+") // crosshairs
        ch.setLocalTranslation( // center
            settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0f
        )
        application.guiNode.attachChild(ch)
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
        hud?.update()

        application.inputManager.isCursorVisible = !mouseCapture
    }
}
