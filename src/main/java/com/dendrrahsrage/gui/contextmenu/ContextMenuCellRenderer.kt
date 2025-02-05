package com.dendrrahsrage.gui.contextmenu

import com.simsilica.lemur.Button
import com.simsilica.lemur.Panel
import com.simsilica.lemur.list.CellRenderer
import com.simsilica.lemur.style.ElementId

class ContextMenuCellRenderer : CellRenderer<ContextMenuAction> {

    var contextMenu: ContextMenu? = null

    override fun configureStyle(elementId: ElementId?, style: String?) {
    }

    override fun getView(value: ContextMenuAction?, selected: Boolean, existing: Panel?): Panel {
        val component = (existing ?: Button(value!!.name)) as Button
        component.addClickCommands {
            value!!.action.invoke()
            contextMenu!!.parent.detachChild(contextMenu)
        }
        return component
    }
}