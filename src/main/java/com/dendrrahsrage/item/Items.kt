package com.dendrrahsrage.item

import com.jme3.asset.AssetManager
import com.jme3.scene.Node

class Items {

    class Burger(assetManager: AssetManager) : Item("Burger", assetManager.loadModel("Models/Kenney/Food/burger.glb") as Node, 1f)
    class Cake(assetManager: AssetManager) : Item("Cake", assetManager.loadModel("Models/Kenney/Food/cake.glb") as Node, 2f)

}