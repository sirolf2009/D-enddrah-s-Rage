/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.gui.hud

import com.dendrrahsrage.entity.EntityPlayer
import com.jme3.app.Application
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.simsilica.lemur.Container
import com.simsilica.lemur.Label
import com.simsilica.lemur.ProgressBar

class HUD(
    app: Application,
    guiNode: Node,
    val player: EntityPlayer,
) {

    val weight = Label("Total weight")
    val location = Label("Location")
    val hp = ProgressBar()
    val hunger = ProgressBar()

    init {
        val topLeft = Container()
        guiNode.attachChild(topLeft)
        topLeft.setLocalTranslation(0f, app.guiViewPort.camera.height.toFloat(), 0f)
        topLeft.addChild(Label("D'endrrah's Rage v 0.0.0.0.0.0.0.0.0.1"))
        topLeft.addChild(location)

        val topRight = Container()
        guiNode.attachChild(topRight)
        topRight.setLocalTranslation(app.guiViewPort.camera.width.toFloat() - 64, app.guiViewPort.camera.height.toFloat(), 0f)
        topRight.addChild(weight)


        val bottomLeft = Container()
        guiNode.attachChild(bottomLeft)
        bottomLeft.setLocalTranslation(0f, 64f, 0f)
        bottomLeft.addChild(hp)
        hp.progressPercent = 1.0
        hp.message = "HP"
        hp.preferredSize = Vector3f(200f, 32f, 1f)
        bottomLeft.addChild(hunger)
        hunger.progressPercent = 1.0
        hunger.message = "Hunger"
        hunger.preferredSize = Vector3f(200f, 32f, 1f)
    }

    fun update() {
        hp.progressValue = player.betterPlayerControl.health
        hunger.progressValue = player.betterPlayerControl.hunger
        weight.text = player.betterPlayerControl.inventory.currentWeight.toString()+"kg"
        location.text = "${player.worldTranslation}"
    }
}
