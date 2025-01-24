package com.dendrrahsrage.gui.hud

import com.dendrrahsrage.item.Inventory
import com.dendrrahsrage.item.Item
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.simsilica.lemur.*
import com.simsilica.lemur.component.BorderLayout
import com.simsilica.lemur.component.BorderLayout.Position
import com.simsilica.lemur.component.QuadBackgroundComponent
import com.simsilica.lemur.core.VersionedList

class InventoryView(
    private val inventory: Inventory
): Container(BorderLayout()) {

    init {
        addChild(Label("Inventory"), Position.North)

        val list = ListBox(VersionedList(inventory.items()))
        addChild(list, Position.Center)

        preferredSize = Vector3f(800f, 600f, 0f)
        background = QuadBackgroundComponent()
    }

}