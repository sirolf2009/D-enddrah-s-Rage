package com.dendrrahsrage.gui

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.gui.contextmenu.ContextMenu
import com.dendrrahsrage.gui.contextmenu.ContextMenuAction
import com.dendrrahsrage.item.Inventory
import com.dendrrahsrage.item.Item
import com.google.common.base.Function
import com.jme3.input.InputManager
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.simsilica.lemur.Button
import com.simsilica.lemur.Container
import com.simsilica.lemur.Label
import com.simsilica.lemur.ListBox
import com.simsilica.lemur.Panel
import com.simsilica.lemur.component.BorderLayout
import com.simsilica.lemur.component.BorderLayout.Position
import com.simsilica.lemur.component.QuadBackgroundComponent
import com.simsilica.lemur.core.VersionedList
import com.simsilica.lemur.list.DefaultCellRenderer

class InventoryView(
    private val inputManager: InputManager,
    private val betterPlayerControl: BetterPlayerControl,
    private val inventory: Inventory,
): Container(BorderLayout()) {

    init {
        val dl = DirectionalLight()
        dl.color = ColorRGBA.White
        dl.direction = Vector3f(0f, 0f, 1f)
        addLight(dl)

        addChild(Label("Inventory"), Position.North)

        val list = ListBox(
            inventory.items(),
            object : DefaultCellRenderer<Item>() {
                override fun valueToString(value: Item?): String {
                    return value!!.name
                }

                override fun getView(value: Item?, selected: Boolean, existing: Panel?): Panel {
                    val button = super.getView(value, selected, existing) as Button
                    button.clickCommands?.clear()
                    button.addClickCommands {
                        println("clicked $value")
                        val contextMenu = ContextMenu(
                            value!!.contextMenuItems(betterPlayerControl, this@InventoryView)
                        )
                        this@InventoryView.parent.attachChild(contextMenu)
                        val mouse = inputManager.cursorPosition
                        contextMenu.setLocalTranslation(
                            mouse.x,
                            mouse.y,
                            2f
                        )
                    }
                    return button
                }
            },
            null
        )
        (list.cellRenderer as DefaultCellRenderer).transform = Function<Item, String> { input -> input.name }
        addChild(list, Position.Center)

        preferredSize = Vector3f(800f, 600f, 0f)
        background = QuadBackgroundComponent()
    }

}