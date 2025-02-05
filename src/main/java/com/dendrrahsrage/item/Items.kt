package com.dendrrahsrage.item

import com.jme3.asset.AssetManager
import com.jme3.scene.Node

class Items {

    class Burger(assetManager: AssetManager) : FoodItem("Burger", assetManager.loadModel("Models/Kenney/Food/burger.glb") as Node, 1f, 60f)
    class Cake(assetManager: AssetManager) : FoodItem("Cake", assetManager.loadModel("Models/Kenney/Food/cake.glb") as Node, 5f, 80f)

    class GreatSword(assetManager: AssetManager) : GreatSwordItem("Great Sword", assetManager.loadModel("Models/GreatSword.glb") as Node, 20f)

}