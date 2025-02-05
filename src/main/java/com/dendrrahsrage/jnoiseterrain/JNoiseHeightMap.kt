package com.dendrrahsrage.jnoiseterrain

import com.jme3.terrain.heightmap.AbstractHeightMap
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import de.articdive.jnoise.transformers.domain_warp.DomainWarpTransformer
import javax.vecmath.Point2f

class JNoiseHeightMap(
    val mapSize: Int = 512,
    val pipeline: JNoise = JNoise.newBuilder()
        .worley(WorleyNoiseGenerator.newBuilder())
        .octavate(10,10.0,1.0,FractalFunction.TURBULENCE,false)
        .addDetailedTransformer(DomainWarpTransformer.newBuilder().setNoiseSource(WorleyNoiseGenerator.newBuilder()).build())
        .scale(0.01)
        .build(),
    val scale: Float = 10f,
    val steepness: Float = 0.08f,
    val featureMultiplier: Float = 4f
) : AbstractHeightMap() {

    override fun load(): Boolean {
        val center = Point2f(mapSize.toFloat().div(2), mapSize.toFloat().div(2))
        this.heightData = (0..<mapSize).flatMap { x ->
            println("Terrain gen $x/$mapSize")
            (0..<mapSize).map { y ->
                val rawValue = pipeline.evaluateNoise(x.toDouble(), y.toDouble()).toFloat()
                val point = Point2f(x.toFloat(), y.toFloat())
                val distance = point.distance(center)
                (rawValue * featureMultiplier + distance * steepness) * scale
            }
        }.toFloatArray()
        return true
    }

}