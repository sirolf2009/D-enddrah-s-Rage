package com.dendrrahsrage.gui.contextmenu

data class ContextMenuAction(
    val name: String,
    val action: () -> Unit
)