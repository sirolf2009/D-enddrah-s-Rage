package com.dendrrahsrage.gui

import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.gui.contextmenu.ContextMenu
import com.dendrrahsrage.item.Inventory
import com.dendrrahsrage.item.Item
import com.google.common.base.Function
import com.jme3.input.InputManager
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.simsilica.lemur.*
import com.simsilica.lemur.component.BorderLayout
import com.simsilica.lemur.component.BorderLayout.Position
import com.simsilica.lemur.component.QuadBackgroundComponent
import com.simsilica.lemur.list.CellRenderer
import com.simsilica.lemur.list.DefaultCellRenderer
import com.simsilica.lemur.style.ElementId

class InventoryView(
    private val inputManager: InputManager,
    private val betterPlayerControl: BetterPlayerControl,
    private val inventory: Inventory,
): Container(BorderLayout()) {

    var contextMenu: ContextMenu? = null

    init {
        val dl = DirectionalLight()
        dl.color = ColorRGBA.White
        dl.direction = Vector3f(0f, 0f, 1f)
        addLight(dl)

        addChild(Label("Inventory"), Position.North)
        val list = ListBox(
            inventory.items(),
            object : CellRenderer<Item> {

                override fun getView(value: Item?, selected: Boolean, existing: Panel?): Panel {
                    val button = if (existing == null) {
                        Button("", elementId, style).also {
                            preferredSize = Vector3f(64f, 64f, 64f)
                        }
                    } else {
                        (existing as Button)
                    }
                    button.background = QuadBackgroundComponent(value!!.icon)
                    button.clickCommands?.clear()
                    button.addClickCommands {
                        println("clicked $value")
                        contextMenu?.removeFromParent()
                        contextMenu = ContextMenu(
                            value.contextMenuItems(betterPlayerControl, this@InventoryView)
                        )
                        this@InventoryView.parent.attachChild(contextMenu)
                        val mouse = inputManager.cursorPosition
                        contextMenu!!.setLocalTranslation(
                            mouse.x,
                            mouse.y,
                            2f
                        )
                    }
                    return button
                }

                override fun configureStyle(elementId: ElementId?, style: String?) {}
            },
            null
        )
        list.preferredSize = Vector3f(64f, 512f, 64f)
        addChild(list, Position.Center)

        preferredSize = Vector3f(800f, 600f, 0f)
        background = QuadBackgroundComponent()
    }

    fun cleanup() {
        contextMenu?.removeFromParent()
    }

}