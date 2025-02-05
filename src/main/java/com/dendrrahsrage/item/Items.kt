package com.dendrrahsrage.item

import com.jme3.asset.AssetManager
import com.jme3.scene.Node

class Items {

    class Burger(assetManager: AssetManager) : FoodItem(
        "Burger",
        assetManager.loadModel("Models/Kenney/Food/burger.glb") as Node,
        assetManager.loadTexture("Textures/Kenney/Food/item-burger.png"),
        1f,
        60f
    )
    class Cake(assetManager: AssetManager) : FoodItem(
        "Cake",
        assetManager.loadModel("Models/Kenney/Food/cake.glb") as Node,
        assetManager.loadTexture("Textures/item-missing.png"),
        2f,
        80f
    )
    class Mushroom(assetManager: AssetManager) : FoodItem(
        "Mushroom",
        assetManager.loadModel("Models/Kenney/Food/mushroom.glb") as Node,
        assetManager.loadTexture("Textures/item-missing.png"),
        0.2f,
        10f
    )
    class Leek(assetManager: AssetManager) : FoodItem(
        "Leek",
        assetManager.loadModel("Models/Kenney/Food/leek.glb") as Node,
        assetManager.loadTexture("Textures/Kenney/Food/item-leek.png"),
        0.8f,
        10f
    )

    class GreatSword(assetManager: AssetManager) : GreatSwordItem(
        "Great Sword",
        assetManager.loadModel("Models/GreatSword.glb") as Node,
        assetManager.loadTexture("Textures/item-missing.png"),
        20f
    )

}