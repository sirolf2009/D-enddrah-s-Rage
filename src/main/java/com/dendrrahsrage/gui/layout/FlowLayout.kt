package com.dendrrahsrage.gui.layout

import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.simsilica.lemur.Panel
import com.simsilica.lemur.component.AbstractGuiComponent
import com.simsilica.lemur.core.GuiControl
import com.simsilica.lemur.core.GuiLayout
import com.simsilica.lemur.core.VersionedList
import kotlin.math.max

class FlowLayout(
    val children: VersionedList<Node> = VersionedList.wrap(mutableListOf())
) : AbstractGuiComponent(), GuiLayout {

    var parent: GuiControl? = null

    override fun calculatePreferredSize(size: Vector3f?) {
        requireNotNull(size)
        var maxHeight = 0f
        var width = 0f
        for (child in children) {
            val childSize = (child as? Panel)?.preferredSize ?: Vector3f.ZERO
            maxHeight = max(maxHeight, childSize.y)
            width += childSize.x
        }
        size.set(width, maxHeight, 1f)
    }

    override fun reshape(pos: Vector3f?, size: Vector3f?) {
        requireNotNull(pos)
        requireNotNull(size)
        var currentRowStartY = pos.y
        var currentRowHeight = 0f
        var currentRowWidth = 0f
        for (child in children) {
            val childSize = (child as? Panel)?.preferredSize ?: Vector3f.ZERO
            if (currentRowWidth + childSize.x > size.x) { // create new row
                currentRowStartY += currentRowHeight
                currentRowHeight = 0f
                currentRowWidth = 0f
            }

            // add child to row
            (child as? Panel)?.setLocalTranslation(pos.x + currentRowWidth, currentRowStartY, 0f)
            child.getControl(GuiControl::class.java).size = Vector3f(childSize.x, childSize.y, childSize.z)
            currentRowWidth += childSize.x
            currentRowHeight = max(currentRowHeight, childSize.y)
        }
    }

    override fun clone(): GuiLayout {
        return FlowLayout(children)
    }

    override fun <T : Node?> addChild(n: T, vararg constraints: Any?): T {
        children.add(n)
        parent!!.node.attachChild(n)
        return n
    }

    override fun removeChild(n: Node?) {
        children.remove(n)
        parent!!.node.detachChild(n)
    }

    override fun getChildren(): MutableCollection<Node> {
        return children.toMutableList()
    }

    override fun clearChildren() {
        ArrayList(children).forEach { removeChild(it) }
        children.clear()
    }

    override fun invalidate() {
        if (parent != null) {
            parent!!.invalidate()
        }
    }

    override fun attach(parent: GuiControl?) {
        super.attach(parent)
        this.parent = parent
        val self = parent!!.node
        for (child in children) {
            self.attachChild(child)
        }
    }
}