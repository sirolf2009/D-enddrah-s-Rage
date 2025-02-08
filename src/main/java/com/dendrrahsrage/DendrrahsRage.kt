package com.dendrrahsrage

import com.dendrrahsrage.appstate.DefaultAppState
import com.dendrrahsrage.appstate.LoadingAppState
import com.dendrrahsrage.appstate.PlayerMovementAppState
import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.control.CurseControl
import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.item.Items
import com.jme3.anim.AnimComposer
import com.jme3.anim.ArmatureMask
import com.jme3.anim.tween.action.ClipAction
import com.jme3.app.SimpleApplication
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

    var mouseCapture = true

    override fun simpleInitApp() {
        instance = this
        GuiGlobals.initialize(this)
        GuiGlobals.getInstance().isCursorEventsEnabled = false
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass")

        getStateManager().attach(
            LoadingAppState(this) { appstate, data ->
                getStateManager().detach(appstate)
                getStateManager().attach(DefaultAppState(this, data))
                getStateManager().attach(PlayerMovementAppState(this, data.player))
            }
        )
    }

    override fun simpleUpdate(tpf: Float) {
        inputManager.isCursorVisible = !mouseCapture
    }

    override fun simpleRender(rm: RenderManager) {
        //add render code here (if any)
    }

    fun getSettings() = settings

    companion object {

        var instance: DendrrahsRage? = null

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
