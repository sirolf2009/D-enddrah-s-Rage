/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dendrrahsrage.appstate;

import com.dendrrahsrage.DendrrahsRage;
import com.dendrrahsrage.actionlistener.WASDMovement;
import com.dendrrahsrage.control.PLayerControl;
import com.dendrrahsrage.gui.hud.HUD;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.simsilica.lemur.GuiGlobals;

public class DefaultAppState extends BulletAppState implements ActionListener {
    
    private final DendrrahsRage app;
    private final Node stateNode;
    private PLayerControl player;

    //Temporary vectors used on each frame.
    //They here to avoid instantiating new vectors on each frame
    final private Vector3f camDir = new Vector3f();
    final private Vector3f camLeft = new Vector3f();
    private Material stone_mat;
    
    private static Sphere sphere;
    
    public DefaultAppState(DendrrahsRage app) {
        super();
        this.app = app;
        stateNode = new Node("Default state");
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application ap) {
        super.initialize(stateManager, app);
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        super.stateAttached(stateManager); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        app.getFlyByCamera().setMoveSpeed(100);
        setUpLight();

        // We load the scene from the zip file and adjust its size.
        app.getAssetManager().registerLocator(
                "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/town.zip",
                HttpZipLocator.class);
        Spatial sceneModel = app.getAssetManager().loadModel("main.scene");
        sceneModel.setLocalScale(2f);

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape(sceneModel);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);

        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, step height, jumping, falling, and gravity.
        // We also put the player in its starting position.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new PLayerControl(this.app, capsuleShape);
        player.setPhysicsLocation(new Vector3f(0, 10, 0));

        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        app.getRootNode().attachChild(sceneModel);
        getPhysicsSpace().add(landscape);
        getPhysicsSpace().add(player);
        
        setDebugEnabled(true);
        
        sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(Sphere.TextureMode.Projected);
        
        stone_mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        stone_mat.setColor("Color", ColorRGBA.Red);
        
        setUpKeys();
        
        new HUD(app, app.getGuiNode());
    }
    
    
    
    public void makeCannonBall() {
        /**
         * Create a cannon ball geometry and attach to scene graph.
         */
        Geometry ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        app.getRootNode().attachChild(ball_geo);
        /**
         * Position the cannon ball
         */
        ball_geo.setLocalTranslation(app.getCamera().getLocation());
        /* Make the ball physical with a mass > 0.0f */
        RigidBodyControl ball_phy = new RigidBodyControl(1f);
        /**
         * Add physical ball to physics space.
         */
        ball_geo.addControl(ball_phy);
        getPhysicsSpace().add(ball_phy);
        /* Accelerate the physical ball to shoot it. */
        ball_phy.setLinearVelocity(app.getCamera().getDirection().mult(25));
    }
    
    private void setUpLight() {
        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        app.getRootNode().addLight(al);
        
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        app.getRootNode().addLight(dl);
    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private void setUpKeys() {
        InputManager inputManager = app.getInputManager();
        WASDMovement wasdMovement = new WASDMovement(player);
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("shoot",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "shoot");
        inputManager.addListener(wasdMovement, "Left", "Right", "Up", "Down", "Jump");
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        app.getRootNode().detachChild(stateNode);
    }

    /**
     * These are our custom actions triggered by key presses. We do not walk
     * yet, we just keep track of the direction the user pressed.
     */
    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("shoot") && !value) {
            makeCannonBall();
        }
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        player.update(tpf);
    }
    
}
