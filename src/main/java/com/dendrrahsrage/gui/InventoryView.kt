package com.dendrrahsrage.gui

import com.dendrrahsrage.control.BetterPlayerControl
import com.dendrrahsrage.gui.contextmenu.ContextMenu
import com.dendrrahsrage.gui.layout.FlowLayout
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
import com.simsilica.lemur.core.GuiControl
import com.simsilica.lemur.core.VersionedReference
import com.simsilica.lemur.list.CellRenderer
import com.simsilica.lemur.list.DefaultCellRenderer
import com.simsilica.lemur.style.ElementId

class InventoryView(
    private val inputManager: InputManager,
    private val betterPlayerControl: BetterPlayerControl,
    private val inventory: Inventory,
): Container(BorderLayout()) {

    var contextMenu: ContextMenu? = null
    var itemList: Container? = null
    lateinit var inventoryRef: VersionedReference<List<Item>>

    init {
        val dl = DirectionalLight()
        dl.color = ColorRGBA.White
        dl.direction = Vector3f(0f, 0f, 1f)
        addLight(dl)

        addChild(Label("Inventory"), Position.North)
        itemList = Container(FlowLayout())
        fillInventoryView()
        addChild(itemList, Position.Center)

        preferredSize = Vector3f(800f, 600f, 0f)
        background = QuadBackgroundComponent()
    }

    fun fillInventoryView() {
        inventory.items().forEach { item ->
            Button("", elementId, style).apply {
                preferredSize = Vector3f(64f, 64f, 64f)
                background = QuadBackgroundComponent(item.icon)
                addClickCommands {
                    contextMenu?.removeFromParent()
                    contextMenu = ContextMenu(
                        item.contextMenuItems(betterPlayerControl, this@InventoryView)
                    )
                    this@InventoryView.parent.attachChild(contextMenu)
                    val mouse = inputManager.cursorPosition
                    contextMenu!!.setLocalTranslation(
                        mouse.x,
                        mouse.y,
                        2f
                    )
                }
            }.let {
                itemList!!.addChild(it)
            }
        }
        itemList!!.getControl(GuiControl::class.java).invalidate()
        inventoryRef = inventory.items().createReference()
    }

    override fun updateLogicalState(tpf: Float) {
        super.updateLogicalState(tpf)
        if(inventoryRef.update()) {
            itemList!!.clearChildren()
            fillInventoryView()
        }
    }

    fun cleanup() {
        contextMenu?.removeFromParent()
    }

}