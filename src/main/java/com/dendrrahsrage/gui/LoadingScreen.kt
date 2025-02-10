package com.dendrrahsrage.gui

import com.dendrrahsrage.appstate.LoadingAppState
import com.jme3.math.Vector3f
import com.simsilica.lemur.Container
import com.simsilica.lemur.Label
import com.simsilica.lemur.component.BorderLayout

class LoadingScreen(
    val loadingAppState: LoadingAppState
) : Container(BorderLayout()) {

    val message: Label

    init {
        message = Label("Loading")
        addChild(message, BorderLayout.Position.Center)
        preferredSize = Vector3f(800f, 600f, 1f)
    }

    override fun updateLogicalState(tpf: Float) {
        super.updateLogicalState(tpf)
        message.text = loadingAppState.message
    }

}
