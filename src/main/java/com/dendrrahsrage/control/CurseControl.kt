package com.dendrrahsrage.control

import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.control.AbstractControl
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

class CurseControl(
    val curseTriggerDistance: Float = 20f,
    val curseDuration: Duration = Duration.parse("PT10S")
) : AbstractControl() {

    var curseLevel: Float? = null
    var isCursed = false
    var cursedAt: Instant? = null

    override fun controlUpdate(tpf: Float) {
        val currentLevel = spatial.worldTranslation.y

        if(!isCursed) {
            if (curseLevel != null) {
                val difference = currentLevel - curseLevel!!
                if (difference >= curseTriggerDistance) {
                    // trigger curse
                    println("Cursed")
                    isCursed = true
                    cursedAt = Instant.now()
                } else if (currentLevel < curseLevel!!) {
                    curseLevel = currentLevel
                }
            } else {
                curseLevel = currentLevel
            }
        } else {
            val timeSinceCurse = cursedAt!!.until(Instant.now())
            if(timeSinceCurse.toKotlinDuration() > curseDuration) {
                isCursed = false
                curseLevel = currentLevel
            }
        }
    }

    override fun controlRender(rm: RenderManager?, vp: ViewPort?) {
        requireNotNull(vp)
        if (isCursed && vp.camera.fov != 8f) {
            vp.camera.fov = 8f
        } else if(!isCursed && vp.camera.fov != 45f) {
            vp.camera.fov = 45f
        }
    }
}