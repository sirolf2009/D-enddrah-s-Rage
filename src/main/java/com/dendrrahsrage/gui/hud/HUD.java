/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.gui.hud;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;

public class HUD {

    public HUD(Application app, Node guiNode) {
        // Create a simple container for our elements
        Container topLeft = new Container();
        guiNode.attachChild(topLeft);

        // Put it somewhere that we will see it.
        // Note: Lemur GUI elements grow down from the upper left corner.
        topLeft.setLocalTranslation(0, app.getGuiViewPort().getCamera().getHeight(), 0);

        // Add some elements
        topLeft.addChild(new Label("D'endrrah's Rage v 0.0.0.0.0.0.0.0.0.1"));
        
        
        Container bottomLeft = new Container();
        guiNode.attachChild(bottomLeft);
        bottomLeft.setLocalTranslation(0, 32, 0);
        ProgressBar hp = new ProgressBar();
        bottomLeft.addChild(hp);
        hp.setProgressPercent(1);
        hp.setMessage("HP");
        hp.setPreferredSize(new Vector3f(200, 32, 1));
    }

}
