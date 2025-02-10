package com.dendrrahsrage.appstate

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.actionlistener.BetterWASDMovement
import com.dendrrahsrage.entity.EntityPlayer
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.font.BitmapText

class PlayerMovementAppState(
    val application: DendrrahsRage,
    val player: EntityPlayer
): AbstractAppState() {

    override fun stateAttached(stateManager: AppStateManager?) {
        super.stateAttached(stateManager)

        application.flyByCamera.isEnabled = false
        player.camNode.isEnabled = true
        application.inputManager.isCursorVisible = false

        BetterWASDMovement(player, application.rootNode, application.guiNode, application, application.inputManager).setupKeys()

        initCrossHairs()
    }

    private fun initCrossHairs() {
        val guiFont = application.assetManager.loadFont("Interface/Fonts/Default.fnt")
        val ch = BitmapText(guiFont)
        ch.setSize(guiFont.charSet.renderedSize * 2f)
        ch.setText("+") // crosshairs
        ch.setLocalTranslation( // center
            application.getSettings().width / 2 - ch.getLineWidth() / 2, application.getSettings().height / 2 + ch.lineHeight / 2, 0f
        )
        application.guiNode.attachChild(ch)
    }

}
