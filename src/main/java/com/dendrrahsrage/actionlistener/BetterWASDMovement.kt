package com.dendrrahsrage.actionlistener

import com.dendrrahsrage.control.BetterPlayerControl
import com.jme3.input.CameraInput
import com.jme3.input.InputManager
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.AnalogListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseAxisTrigger
import com.jme3.math.Matrix3f
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f

class BetterWASDMovement(
    private val playerControl: BetterPlayerControl
): ActionListener, AnalogListener {

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

        // both mouse and button - rotation of cam
        inputManager.addMapping(
            CameraInput.FLYCAM_LEFT, MouseAxisTrigger(MouseInput.AXIS_X, true)
        )

        inputManager.addMapping(
            CameraInput.FLYCAM_RIGHT, MouseAxisTrigger(MouseInput.AXIS_X, false)
        )

        inputManager.addMapping(
            CameraInput.FLYCAM_UP, MouseAxisTrigger(MouseInput.AXIS_Y, false)
        )

        inputManager.addMapping(
            CameraInput.FLYCAM_DOWN, MouseAxisTrigger(MouseInput.AXIS_Y, true)
        )
        inputManager.addListener(this,  CameraInput.FLYCAM_LEFT, CameraInput.FLYCAM_RIGHT, CameraInput.FLYCAM_UP, CameraInput.FLYCAM_DOWN)
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

    protected var rotationSpeedX: Float = 1f
    protected var rotationSpeedY: Float = 5f

    protected fun rotateCamera(value: Float, axis: Vector3f) {
        val mat = Matrix3f()
        mat.fromAngleNormalAxis(rotationSpeedX * value, axis)

        val viewDir: Vector3f = playerControl.viewDirection

        mat.mult(viewDir, viewDir)

        playerControl.viewDirection = viewDir
    }

    override fun onAnalog(name: String?, value: Float, tpf: Float) {
        if ((name == CameraInput.FLYCAM_LEFT)) {
            rotateCamera(value, Vector3f.UNIT_Y)
        } else if ((name == CameraInput.FLYCAM_RIGHT)) {
            rotateCamera(-value, Vector3f.UNIT_Y)
        } else if ((name == CameraInput.FLYCAM_UP)) {
            playerControl.camera.localTranslation = playerControl.camera.localTranslation.add(0f, -value * rotationSpeedY, 0f)
        } else if ((name == CameraInput.FLYCAM_DOWN)) {
            playerControl.camera.localTranslation = playerControl.camera.localTranslation.add(0f, value * rotationSpeedY, 0f)
        }
    }
}