package com.dendrrahsrage.actionlistener

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.Interactable
import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.control.player.PlaceItem
import com.dendrrahsrage.gui.InventoryView
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.collision.CollisionResults
import com.jme3.input.CameraInput
import com.jme3.input.InputManager
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.*
import com.jme3.math.Matrix3f
import com.jme3.math.Ray
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.simsilica.lemur.GuiGlobals


class BetterWASDMovement(
    private val player: EntityPlayer,
    private val sceneNode: Node,
    private val application: DendrrahsRage,
    private val inputManager: InputManager
): ActionListener, AnalogListener {

    fun setupKeys() {
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
        inputManager.addMapping("Interact", KeyTrigger(KeyInput.KEY_E))
        inputManager.addMapping("Action", MouseButtonTrigger(MouseInput.BUTTON_LEFT))
        inputManager.addListener(this, "Strafe Left", "Strafe Right")
        inputManager.addListener(this, "Rotate Left", "Rotate Right")
        inputManager.addListener(this, "Walk Forward", "Walk Backward")
        inputManager.addListener(this, "Jump", "Duck")
        inputManager.addListener(this, "Interact", "Action")

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
            player.betterPlayerControl.leftStrafe = value
        } else if (binding.equals("Strafe Right")) {
            player.betterPlayerControl.rightStrafe = value
        } else if (binding.equals("Rotate Left")) {
            player.betterPlayerControl.leftRotate = value
        } else if (binding.equals("Rotate Right")) {
            player.betterPlayerControl.rightRotate = value
        } else if (binding.equals("Walk Forward")) {
            player.betterPlayerControl.forward = value
        } else if (binding.equals("Walk Backward")) {
            player.betterPlayerControl.backward = value
        } else if (binding.equals("Jump")) {
            player.betterPlayerControl.jump()
        } else if (binding.equals("Duck")) {
            if (value) {
                player.betterPlayerControl.isDucked = true
            } else {
                player.betterPlayerControl.isDucked = false
            }
        } else if (binding.equals("Interact") && !value) {
            val results = CollisionResults()
            val ray = Ray(player.camNode.camera.getLocation(), player.camNode.camera.getDirection())
            sceneNode.collideWith(ray, results)

            if (results.size() > 0) {
                val closest = results.closestCollision
                if(closest.distance < 15f) {
                    interactable(closest.geometry.parent)?.interact(player)
                }
            }
        } else if(binding.equals("Action") && !value) {
            if(player.betterPlayerControl.action != null) {
                player.betterPlayerControl.action!!.actionExecute()
            } else {
                player.betterPlayerControl.attack()
            }
        }
    }

    fun interactable(node: Node): Interactable? {
        if(node is Interactable) {
            return node
        }
        if(node.parent != null) {
            return interactable(node.parent)
        }
        return null
    }

    protected var rotationSpeedX: Float = 1f
    protected var rotationSpeedY: Float = 5f

    protected fun rotateCamera(value: Float, axis: Vector3f) {
        val mat = Matrix3f()
        mat.fromAngleNormalAxis(rotationSpeedX * value, axis)

        val viewDir: Vector3f = player.betterPlayerControl.viewDirection

        mat.mult(viewDir, viewDir)

        player.betterPlayerControl.viewDirection = viewDir
    }

    override fun onAnalog(name: String?, value: Float, tpf: Float) {
        if (application.mouseCapture) {
            if ((name == CameraInput.FLYCAM_LEFT)) {
                rotateCamera(value, Vector3f.UNIT_Y)
            } else if ((name == CameraInput.FLYCAM_RIGHT)) {
                rotateCamera(-value, Vector3f.UNIT_Y)
            } else if ((name == CameraInput.FLYCAM_UP)) {
                player.camNode.localTranslation =
                    player.camNode.localTranslation.add(0f, -value * rotationSpeedY, 0f)
            } else if ((name == CameraInput.FLYCAM_DOWN)) {
                player.camNode.localTranslation =
                    player.camNode.localTranslation.add(0f, value * rotationSpeedY, 0f)
            }
        }
    }
}
