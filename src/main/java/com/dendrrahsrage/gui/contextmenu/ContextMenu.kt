package com.dendrrahsrage.gui.contextmenu

import com.simsilica.lemur.ListBox
import com.simsilica.lemur.core.VersionedList

class ContextMenu(
    actions: List<ContextMenuAction>
) : ListBox<ContextMenuAction>(
    VersionedList.wrap(actions),
    ContextMenuCellRenderer(),
    null
) {

    init {
        (cellRenderer as ContextMenuCellRenderer).contextMenu = this
    }

}