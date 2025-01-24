/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.control;

import com.jme3.app.Application;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class PlayerControl extends CharacterControl {
    
    private final Application application;
    private final Node mesh;
    private final Vector3f camDir = new Vector3f();
    private final Vector3f camLeft = new Vector3f();
    
    public boolean left = false, right = false, up = false, down = false;
    private final Vector3f tmpWalkDirection = new Vector3f();
    
    private double health = 100;

    public PlayerControl(Application application, CollisionShape shape, Node mesh) {
        super(shape, 0.05f);
        this.application = application;
        this.mesh = mesh;
        setJumpSpeed(20);
        setFallSpeed(30);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        camDir.set(application.getCamera().getDirection()).multLocal(1f);
        camLeft.set(application.getCamera().getLeft()).multLocal(1f);
        tmpWalkDirection.set(0, 0, 0);
        if (left) {
            tmpWalkDirection.addLocal(camLeft);
        }
        if (right) {
            tmpWalkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            tmpWalkDirection.addLocal(camDir);
        }
        if (down) {
            tmpWalkDirection.addLocal(camDir.negate());
        }
        setWalkDirection(tmpWalkDirection);
        application.getCamera().setLocation(getPhysicsLocation().subtract(camDir.normalize().mult(10f)));
        mesh.setLocalRotation(application.getCamera().getRotation());
    }

}
