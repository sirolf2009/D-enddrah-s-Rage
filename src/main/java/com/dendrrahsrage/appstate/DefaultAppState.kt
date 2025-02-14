/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.appstate

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.control.player.BetterPlayerControl
import com.dendrrahsrage.control.*
import com.dendrrahsrage.entity.EntityCow
import com.dendrrahsrage.gui.hud.HUD
import com.dendrrahsrage.item.Items
import com.jme3.app.Application
import com.jme3.app.state.AppStateManager
import com.jme3.bullet.BulletAppState
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Node

class DefaultAppState(
    val application: DendrrahsRage,
    val loadedData: LoadingAppState.LoadedData
) : BulletAppState() {
    private val stateNode: Node
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

        physicsSpace.setGravity(Vector3f(0f, -9.8f, 0f))

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        application.viewPort.backgroundColor = ColorRGBA(0.7f, 0.8f, 1f, 1f)
        setUpLight()


        stateNode.attachChild(loadedData.terrain)
        physicsSpace.add(loadedData.terrain)

        loadedData.terrain.addControl(GrowPlantsControl(GrowPlantsControl.defaultPlants(application.assetManager), physicsSpace))

        setupBetterPlayer()

        hud = HUD(application, application.getGuiNode(), loadedData.player)

        application.rootNode.attachChild(stateNode)

        isDebugEnabled = false

        val cow = EntityCow(
            loadedData.terrain,
            application.assetManager
        )
        cow.setLocationOnTerrain(201f, 200f)
        physicsSpace.add(cow)
        stateNode.attachChild(cow)
    }

    fun setupBetterPlayer() {
        stateNode.attachChild(loadedData.player)
        physicsSpace.add(loadedData.player.betterPlayerControl)

        loadedData.player.setLocationOnTerrain(200f, 200f)
        loadedData.player.betterPlayerControl.equip(Items.GreatSword(application.assetManager))
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
