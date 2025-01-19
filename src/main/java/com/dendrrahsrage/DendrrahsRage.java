package com.dendrrahsrage;

import com.dendrrahsrage.appstate.DefaultAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;

/**
 * This is the Main Class of your Game. It should boot up your game and do initial initialisation
 * Move your Logic into AppStates or Controls or other java classes
 */
public class DendrrahsRage extends SimpleApplication {

    public static void main(String[] args) {
        DendrrahsRage app = new DendrrahsRage();
        app.setShowSettings(false); //Settings dialog not supported on mac
        AppSettings settings = new AppSettings(true);
        settings.setTitle("D'endrrah's Rage");
        settings.setWindowSize(1024, 800);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        GuiGlobals.getInstance().setCursorEventsEnabled(false);

        getInputManager().setCursorVisible(false);
        getStateManager().attach(new DefaultAppState(this));
    }

    @Override
    public void simpleUpdate(float tpf) {
        //this method will be called every game tick and can be used to make updates
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //add render code here (if any)
    }
}
