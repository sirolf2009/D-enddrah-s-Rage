package com.dendrrahsrage.actionlistener

import com.dendrrahsrage.control.BetterPlayerControl
import com.jme3.input.FlyByCamera
import com.jme3.input.InputManager
import com.jme3.input.KeyInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.scene.CameraNode

class BetterWASDMovement(
    private val playerControl: BetterPlayerControl
): ActionListener {

    private var lockView = false

    fun setupKeys(inputManager: InputManager) {
        inputManager.addMapping("Strafe Left",
            KeyTrigger(KeyInput.KEY_A),
            KeyTrigger(KeyInput.KEY_Z))
        inputManager.addMapping("Strafe Right",
            KeyTrigger(KeyInput.KEY_D),
            KeyTrigger(KeyInput.KEY_X))
        inputManager.addMapping("Rotate Left",
            KeyTrigger(KeyInput.KEY_J),
            KeyTrigger(KeyInput.KEY_LEFT))
        inputManager.addMapping("Rotate Right",
            KeyTrigger(KeyInput.KEY_L),
            KeyTrigger(KeyInput.KEY_RIGHT))
        inputManager.addMapping("Walk Forward",
            KeyTrigger(KeyInput.KEY_W),
            KeyTrigger(KeyInput.KEY_UP))
        inputManager.addMapping("Walk Backward",
            KeyTrigger(KeyInput.KEY_S),
            KeyTrigger(KeyInput.KEY_DOWN))
        inputManager.addMapping("Jump",
            KeyTrigger(KeyInput.KEY_F),
            KeyTrigger(KeyInput.KEY_SPACE))
        inputManager.addMapping("Duck",
            KeyTrigger(KeyInput.KEY_G),
            KeyTrigger(KeyInput.KEY_LSHIFT),
            KeyTrigger(KeyInput.KEY_RSHIFT))
        inputManager.addListener(this, "Strafe Left", "Strafe Right")
        inputManager.addListener(this, "Rotate Left", "Rotate Right")
        inputManager.addListener(this, "Walk Forward", "Walk Backward")
        inputManager.addListener(this, "Jump", "Duck")
    }

    override fun onAction(binding: String?, value: Boolean, tpf: Float) {
        if (binding.equals("Strafe Left")) {
            playerControl.leftStrafe = value
        } else if (binding.equals("Strafe Right")) {
            playerControl.rightStrafe = value
        } else if (binding.equals("Rotate Left")) {
            playerControl.leftRotate = value
        } else if (binding.equals("Rotate Right")) {
            playerControl.rightRotate = value
        } else if (binding.equals("Walk Forward")) {
            playerControl.forward = value
        } else if (binding.equals("Walk Backward")) {
            playerControl.backward = value
        } else if (binding.equals("Jump")) {
            playerControl.jump()
        } else if (binding.equals("Duck")) {
            if (value) {
                playerControl.isDucked = true
            } else {
                playerControl.isDucked = false
            }
        }
    }
}