package com.dendrrahsrage.util

import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Mesh
import com.jme3.scene.Node
import com.jme3.scene.debug.Arrow

class Gizmo(val assetManager: AssetManager) : Node("Gizmo") {

    init {
        var arrow = Arrow(Vector3f.UNIT_X)
        attachChild(createArrow(arrow, ColorRGBA.Red))

        arrow = Arrow(Vector3f.UNIT_Y)
        attachChild(createArrow(arrow, ColorRGBA.Green))

        arrow = Arrow(Vector3f.UNIT_Z)
        attachChild(createArrow(arrow, ColorRGBA.Blue))
    }

    private fun createArrow(shape: Mesh, color: ColorRGBA): Geometry {
        val g = Geometry("coordinate axis", shape)
        val mat = Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
        mat.additionalRenderState.isWireframe = true
        mat.additionalRenderState.lineWidth = 4f
        mat.setColor("Color", color)
        g.material = mat
        return g
    }

}
