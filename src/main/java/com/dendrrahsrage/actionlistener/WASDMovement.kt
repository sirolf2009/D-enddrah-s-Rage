/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.actionlistener

import com.dendrrahsrage.control.PlayerControl
import com.jme3.input.InputManager
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseButtonTrigger

class WASDMovement(val playerControl: PlayerControl) : ActionListener {

    fun setUpKeys(inputManager: InputManager) {
        inputManager.addMapping("Left", KeyTrigger(KeyInput.KEY_A))
        inputManager.addMapping("Right", KeyTrigger(KeyInput.KEY_D))
        inputManager.addMapping("Up", KeyTrigger(KeyInput.KEY_W))
        inputManager.addMapping("Down", KeyTrigger(KeyInput.KEY_S))
        inputManager.addMapping("Jump", KeyTrigger(KeyInput.KEY_SPACE))
        inputManager.addListener(this, "Left", "Right", "Up", "Down", "Jump")
    }

    override fun onAction(name: String, isPressed: Boolean, tpf: Float) {
        if (name == "Left") {
            if (isPressed) {
                playerControl.left = true
            } else {
                playerControl.left = false
            }
        } else if (name == "Right") {
            if (isPressed) {
                playerControl.right = true
            } else {
                playerControl.right = false
            }
        } else if (name == "Up") {
            if (isPressed) {
                playerControl.up = true
            } else {
                playerControl.up = false
            }
        } else if (name == "Down") {
            if (isPressed) {
                playerControl.down = true
            } else {
                playerControl.down = false
            }
        } else if (name == "Jump") {
            playerControl.jump()
        }
    }
}
