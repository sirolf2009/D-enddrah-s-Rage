/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.appstate

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.control.PlayerControl
import com.dendrrahsrage.control.*
import com.dendrrahsrage.gui.hud.HUD
import com.dendrrahsrage.jnoiseterrain.JNoiseHeightMap
import com.jme3.app.Application
import com.jme3.app.state.AppStateManager
import com.jme3.bullet.BulletAppState
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.jme3.terrain.geomipmap.TerrainLodControl
import com.jme3.terrain.geomipmap.TerrainQuad
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator
import com.jme3.terrain.heightmap.AbstractHeightMap
import com.jme3.texture.Texture
import com.jme3.texture.Texture.WrapMode

class DefaultAppState(
    val application: DendrrahsRage
) : BulletAppState() {
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
        super.stateAttached(stateManager)

        physicsSpace.setGravity(Vector3f(0f, -20f, 0f))

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
        stateNode.attachChild(terrain)
        physicsSpace.add(terrain)


        /** 5. The LOD (level of detail) depends on were the camera is:  */
        val control: TerrainLodControl = TerrainLodControl(terrain, application.camera)
        control.setLodCalculator(DistanceLodCalculator(patchSize, 2.7f)) // patch size, and a multiplier
        terrain.addControl(control)

        terrain.addControl(GrowPlantsControl(GrowPlantsControl.defaultPlants(application.assetManager), physicsSpace))

        setupBetterPlayer(terrain)

        hud = HUD(application, application.getGuiNode(), application.player)

        application.rootNode.attachChild(stateNode)

        isDebugEnabled = true

        val cowNode = Node("Cow")
        val cowModel = application.assetManager.loadModel("Models/cow.glb") as Node
        cowNode.attachChild(cowModel)
        cowNode.addControl(RigidBodyControl(1000f))
        cowNode.addControl(HealthControl())
        val cowX = 201f
        val cowY = 200f
        cowNode.setLocalTranslation(cowX, terrain.getHeight(Vector2f(cowX, cowY)) + 1, cowY)
        cowNode.getControl(RigidBodyControl::class.java).physicsLocation = Vector3f(cowX, terrain.getHeight(Vector2f(cowX, cowY)) + 1, cowY)
        physicsSpace.add(cowNode)
        stateNode.attachChild(cowNode)
    }

    fun setupBetterPlayer(terrainQuad: TerrainQuad) {
        stateNode.attachChild(application.player.node)
        physicsSpace.add(application.player.getPlayerControl())

        val characterX = 200f
        val characterY = 200f
        application.player.node.setLocalTranslation(characterX, terrainQuad.getHeight(Vector2f(characterX, characterY)) + 1, characterY)
        application.player.getPlayerControl().getRigidBody().physicsLocation = Vector3f(characterX, terrainQuad.getHeight(Vector2f(characterX, characterY)) + 1, characterY)
    }

    private fun setUpLight() {
        // We add light so we see the scene
        val al = AmbientLight()
        al.color = ColorRGBA.White.mult(1.3f)
        stateNode.addLight(al)

        val dl = DirectionalLight()
        dl.color = ColorRGBA.White
        dl.direction = Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal()
        stateNode.addLight(dl)
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
