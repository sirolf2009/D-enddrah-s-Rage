/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.actionlistener;

import com.dendrrahsrage.control.PLayerControl;
import com.jme3.input.controls.ActionListener;

public class WASDMovement implements ActionListener {
    
    public final PLayerControl playerControl;

    public WASDMovement(PLayerControl playerControl) {
        this.playerControl = playerControl;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Left")) {
            if (isPressed) {
                playerControl.left = true;
            } else {
                playerControl.left = false;
            }
        } else if (name.equals("Right")) {
            if (isPressed) {
                playerControl.right = true;
            } else {
                playerControl.right = false;
            }
        } else if (name.equals("Up")) {
            if (isPressed) {
                playerControl.up = true;
            } else {
                playerControl.up = false;
            }
        } else if (name.equals("Down")) {
            if (isPressed) {
                playerControl.down = true;
            } else {
                playerControl.down = false;
            }
        } else if (name.equals("Jump")) {
            playerControl.jump();
        }
    }
    
}
