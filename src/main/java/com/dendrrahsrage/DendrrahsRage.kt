package com.dendrrahsrage

import com.dendrrahsrage.appstate.DefaultAppState
import com.jme3.app.SimpleApplication
import com.jme3.renderer.RenderManager
import com.jme3.system.AppSettings
import com.simsilica.lemur.GuiGlobals

/**
 * This is the Main Class of your Game. It should boot up your game and do initial initialisation
 * Move your Logic into AppStates or Controls or other java classes
 */
class DendrrahsRage : SimpleApplication() {
    override fun simpleInitApp() {
        GuiGlobals.initialize(this)
        GuiGlobals.getInstance().isCursorEventsEnabled = false

        getStateManager().attach(DefaultAppState(this, settings))
    }

    override fun simpleUpdate(tpf: Float) {
        //this method will be called every game tick and can be used to make updates
    }

    override fun simpleRender(rm: RenderManager) {
        //add render code here (if any)
    }

    fun main() {
        val app = DendrrahsRage()
        app.isShowSettings = false //Settings dialog not supported on mac
        val settings = AppSettings(true)
        settings.title = "D'endrrah's Rage"
        settings.setWindowSize(1024, 800)
        app.setSettings(settings)
        app.start()
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun main(args: Array<String>) {
            val app = DendrrahsRage()
            app.isShowSettings = false //Settings dialog not supported on mac
            val settings = AppSettings(true)
            settings.title = "D'endrrah's Rage"
            settings.setWindowSize(1024, 800)
            app.setSettings(settings)
            app.start()
        }
    }
}
