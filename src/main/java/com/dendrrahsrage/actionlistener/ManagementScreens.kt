package com.dendrrahsrage.actionlistener

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.crafting.CraftingRecipes
import com.dendrrahsrage.entity.EntityPlayer
import com.dendrrahsrage.gui.CraftingScreen
import com.dendrrahsrage.gui.InventoryView
import com.jme3.input.InputManager
import com.jme3.input.KeyInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.scene.Node
import com.simsilica.lemur.GuiGlobals

class ManagementScreens(
    val application: DendrrahsRage,
    val player: EntityPlayer,
    val guiNode: Node,
    val inputManager: InputManager = application.inputManager,
): ActionListener {

    var inventoryView: InventoryView? = null
    var craftingScreen: CraftingScreen? = null

    fun setupKeys() {
        inputManager.addMapping("Inventory", KeyTrigger(KeyInput.KEY_TAB))
        inputManager.addMapping("Crafting", KeyTrigger(KeyInput.KEY_C))

        inputManager.addListener(this, "Inventory", "Crafting")
    }

    override fun onAction(binding: String?, isPressed: Boolean, tpf: Float) {
        requireNotNull(binding)
        if(binding == "Inventory" && !isPressed) {
            if(inventoryView == null) {
                inventoryView = InventoryView(inputManager, player, player.betterPlayerControl.inventory)
                inventoryView!!.setLocalTranslation(20f, 700f, 0f)
                guiNode.attachChild(inventoryView)
                application.mouseCapture = false
                GuiGlobals.getInstance().isCursorEventsEnabled = true
            } else {
                closeInventoryView()
            }
        } else if(binding == "Crafting" && !isPressed) {
            if(craftingScreen == null) {
                craftingScreen = CraftingScreen(player, CraftingRecipes.getRecipes(application.assetManager))
                craftingScreen!!.setLocalTranslation(20f, 700f, 0f)
                guiNode.attachChild(craftingScreen)
                application.mouseCapture = false
                GuiGlobals.getInstance().isCursorEventsEnabled = true
            } else {
                closeCraftingScreen()
            }
        }
    }

    fun closeInventoryView() {
        inventoryView!!.cleanup()
        guiNode.detachChild(inventoryView)
        inventoryView = null
        application.mouseCapture = true
        GuiGlobals.getInstance().isCursorEventsEnabled = false
    }

    fun closeCraftingScreen() {
        craftingScreen!!.cleanup()
        guiNode.detachChild(craftingScreen)
        craftingScreen = null
        application.mouseCapture = true
        GuiGlobals.getInstance().isCursorEventsEnabled = false
    }

}