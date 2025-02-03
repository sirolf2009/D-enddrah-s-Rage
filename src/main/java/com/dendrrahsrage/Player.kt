package com.dendrrahsrage

import com.dendrrahsrage.control.BetterPlayerControl
import com.jme3.anim.AnimComposer
import com.jme3.scene.CameraNode
import com.jme3.scene.Node

data class Player(
    val node: Node
) {

    fun getPlayerControl() = node.getControl(BetterPlayerControl::class.java)
    fun getAnimCompose() = getModel().getChild(0).getControl(AnimComposer::class.java)
    fun getCameraNode() = node.getChild(CameraNodeName) as CameraNode
    fun getModel() = node.getChild("Scene") as Node

    companion object {

        val CameraNodeName = "CamNode"

    }

}