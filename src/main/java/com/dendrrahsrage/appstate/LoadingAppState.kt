package com.dendrrahsrage.appstate

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.gui.LoadingScreen
import com.dendrrahsrage.terrain.TerrainPit
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.asset.AssetManager
import com.jme3.renderer.Camera

class LoadingAppState(
    val application: DendrrahsRage,
    val callback: (LoadingAppState, LoadedData) -> Unit,
) : AbstractAppState() {

    lateinit var loadingScreen: LoadingScreen
    var message: String? = null

    override fun stateAttached(stateManager: AppStateManager?) {
        super.stateAttached(stateManager)
        loadingScreen = LoadingScreen(this)
        application.guiNode.attachChild(loadingScreen)
        loadingScreen.setLocalTranslation(20f, 700f, 0f)
        Thread {
            message = "Loading terrain"
            val terrain = TerrainPit(application.assetManager)
            message = "Loading player"
            val player = EntityPlayer(
                terrain,
                application.assetManager,
                application.camera,
            )
            message = "Done loading"
            callback.invoke(this, LoadedData(
                terrain = terrain,
                player = player,
            ))
        }.start()
    }

    override fun stateDetached(stateManager: AppStateManager?) {
        super.stateDetached(stateManager)
        application.guiNode.detachChild(loadingScreen)
    }

    data class LoadedData(
        val terrain: TerrainPit,
        val player: EntityPlayer
    )

}
