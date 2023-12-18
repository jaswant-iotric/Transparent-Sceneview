package com.iotric.filament

import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.View
import dev.romainguy.kotlin.math.Float2
import io.github.sceneview.SceneView
import io.github.sceneview.collision.Vector3
import io.github.sceneview.math.Position
import io.github.sceneview.math.toCollisionRay
import io.github.sceneview.node.ModelNode
import io.github.sceneview.utils.viewToRay


class Test2 : AppCompatActivity() {
    private lateinit var sceneView: SceneView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        setupTransparentSceneView()
        addSampleNode()
    }

    private fun addSampleNode() {
        val modelLoader = sceneView.modelLoader
        val node = ModelNode(modelLoader.createModelInstance("models/damaged_helmet.glb")).apply {
            position = Position(z = -4.0f, x = 2.14289f, y = 0.0f)
            centerOrigin(Position(0.0f))
        }

        sceneView.addChildNode(node)
        sceneView.onGestureListener = null

        Log.d("Hello--->", "${sceneView.cameraNode.worldToView(node.position) }")
        Log.d("Hello2--->", "${sceneView.cameraNode.viewToWorld(Float2(1f, 0.5f), 0f) }")


        moveNodeTo(node, 0.5f , //Center X (50% of Screen),
     1f // Top Of Screen
        )
    }

//    sceneView.cameraNode.worldToView(Position(1f, 1f, -4f))
////1.2, 0.2666
////0.998f
//    sceneView.cameraNode.viewToWorld(Float2(1f, 0.26666f), 0.998f)

    private fun moveNodeTo(node: ModelNode, screenX: Float, screenY: Float) {
        val pos = sceneView.cameraNode.viewToWorld(Float2(screenX, screenY), 0.998f)
        node.position = pos
    }

    private fun setupTransparentSceneView() {
        sceneView = findViewById(R.id.mSceneView)
        sceneView.lifecycle = this.lifecycle

        sceneView.setZOrderOnTop(true)
        sceneView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        sceneView.holder.setFormat(PixelFormat.TRANSLUCENT)
        sceneView.uiHelper.isOpaque = false
        sceneView.view.blendMode = com.google.android.filament.View.BlendMode.TRANSLUCENT
        sceneView.scene.skybox = null

        val options = sceneView.renderer.clearOptions
        options.clear = true
        sceneView.renderer.clearOptions = options


        sceneView.view.dynamicResolutionOptions =
            View.DynamicResolutionOptions().apply {
                enabled = true
                quality = View.QualityLevel.LOW
            }
    }
}