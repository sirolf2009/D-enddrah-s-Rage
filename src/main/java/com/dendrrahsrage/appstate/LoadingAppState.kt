package com.dendrrahsrage.appstate

import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.terrain.TerrainPit
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.asset.AssetManager
import com.jme3.renderer.Camera

class LoadingAppState(
    val assetManager: AssetManager,
    val camera: Camera,
    val callback: (LoadingAppState, LoadedData) -> Unit,
) : AbstractAppState() {

    override fun update(tpf: Float) {
        super.update(tpf)
    }

    override fun stateAttached(stateManager: AppStateManager?) {
        super.stateAttached(stateManager)

        Thread {
            val terrain = TerrainPit(assetManager)
            val player = EntityPlayer(
                terrain,
                assetManager,
                camera,
            )
            callback.invoke(this, LoadedData(
                terrain = terrain,
                player = player,
            ))
        }.start()
    }

    data class LoadedData(
        val terrain: TerrainPit,
        val player: EntityPlayer
    )

}
