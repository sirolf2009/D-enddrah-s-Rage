package com.dendrrahsrage.terrain

import com.dendrrahsrage.DendrrahsRage
import com.dendrrahsrage.control.GrowPlantsControl
import com.dendrrahsrage.jnoiseterrain.JNoiseHeightMap
import com.jme3.asset.AssetManager
import com.jme3.bullet.control.RigidBodyControl
import com.jme3.material.Material
import com.jme3.terrain.geomipmap.TerrainLodControl
import com.jme3.terrain.geomipmap.TerrainQuad
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator
import com.jme3.texture.Texture
import com.jme3.texture.Texture.WrapMode

class TerrainPit(
    val assetManager: AssetManager,
) : TerrainQuad("my terrain", 65, 513, getHeightMap()) {

    init {
        val mat_terrain = Material(
            assetManager,
            "Common/MatDefs/Terrain/Terrain.j3md"
        )


        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures)  */
        mat_terrain.setTexture(
            "Alpha", assetManager.loadTexture(
                "Textures/Terrain/splat/alphamap.png"
            )
        )

        /** 1.2) Add GRASS texture into the red layer (Tex1).  */
        val grass: Texture = assetManager.loadTexture(
            "Textures/Terrain/splat/grass.jpg"
        )
        grass.setWrap(WrapMode.Repeat)
        mat_terrain.setTexture("Tex1", grass)
        mat_terrain.setFloat("Tex1Scale", 64f)


        /** 1.3) Add DIRT texture into the green layer (Tex2)  */
        val dirt: Texture = assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg"
        )
        dirt.setWrap(WrapMode.Repeat)
        mat_terrain.setTexture("Tex2", dirt)
        mat_terrain.setFloat("Tex2Scale", 32f)


        /** 1.4) Add ROAD texture into the blue layer (Tex3)  */
        val rock: Texture = assetManager.loadTexture(
            "Textures/Terrain/splat/road.jpg"
        )
        rock.setWrap(WrapMode.Repeat)
        mat_terrain.setTexture("Tex3", rock)
        mat_terrain.setFloat("Tex3Scale", 128f)


        /** 3. We have prepared material and heightmap.
         * Now we create the actual terrain:
         * 3.1) Create a TerrainQuad and name it "my terrain".
         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
         * 3.5) We supply the prepared heightmap itself.
         */

        /** 4. We give the terrain its material, position & scale it, and attach it.  */
        setMaterial(mat_terrain)
        //terrain.setLocalTranslation(40f, -50f, 0f)
        setLocalScale(4f, 4f, 4f)

        addControl(RigidBodyControl(0f))

        /** 5. The LOD (level of detail) depends on were the camera is:  */
        val control: TerrainLodControl = TerrainLodControl(this, DendrrahsRage.instance!!.camera)
        control.setLodCalculator(DistanceLodCalculator(patchSize, 2.7f)) // patch size, and a multiplier
        addControl(control)
    }


    companion object {

        fun getHeightMap(): FloatArray? {
            val heightmap = JNoiseHeightMap()
            heightmap.load()
            return heightmap.heightMap
        }

    }
}
