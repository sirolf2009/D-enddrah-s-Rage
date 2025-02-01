package com.dendrrahsrage.item

import com.jme3.asset.AssetManager
import com.jme3.scene.Node

class Items {

    class Burger(assetManager: AssetManager) : FoodItem("Burger", assetManager.loadModel("Models/Kenney/Food/burger.glb") as Node, 1f, 60f)
    class Cake(assetManager: AssetManager) : FoodItem("Cake", assetManager.loadModel("Models/Kenney/Food/cake.glb") as Node, 2f, 80f)
    class Mushroom(assetManager: AssetManager) : FoodItem("Mushroom", assetManager.loadModel("Models/Kenney/Food/mushroom.glb") as Node, 0.2f, 10f)
    class Leek(assetManager: AssetManager) : FoodItem("Leek", assetManager.loadModel("Models/Kenney/Food/leek.glb") as Node, 0.8f, 10f)

}