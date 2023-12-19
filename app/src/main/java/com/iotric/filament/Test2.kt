package com.iotric.filament

import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.View
import dev.romainguy.kotlin.math.Float2
import io.github.sceneview.SceneView
import io.github.sceneview.collision.Vector3
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.math.toCollisionRay
import io.github.sceneview.node.ModelNode
import io.github.sceneview.utils.viewToRay


class Test2 : AppCompatActivity() {
    private lateinit var sceneView: SceneView
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        setupTransparentSceneView()
        calculateScreenMatrices()
        addSampleNode()

        sceneView.setOnTouchListener { _, _ -> true }
    }

    private fun addSampleNode() {
        val modelLoader = sceneView.modelLoader
        val node = ModelNode(
            modelLoader.createModelInstance("models/damaged_helmet.glb")
            //modelLoader.createModelInstance(R.raw.recon_helmet)
        ).apply {
           // position = Position(z = -4.0f, x = 2.14289f, y = 0.0f)
            scale = Scale(0.2f)
           // centerOrigin(Position(0.5f))
        }

        sceneView.addChildNode(node)
        sceneView.onGestureListener = null

       // Log.d("Hello--->", "${sceneView.cameraNode.worldToView(node.position) }")
       // Log.d("Hello2--->", "${sceneView.cameraNode.viewToWorld(Float2(1f, 0.5f), 0f) }")

        moveNodeTo(node, 1f , //Center X (50% of Screen),
     0.5f // Top Of Screen
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            calculateScreenMatrices()
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            calculateScreenMatrices()
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNodePositions() {
        moveNodeTo(
            sceneView.childNodes[0] as ModelNode, 1f , //Center X (50% of Screen),
            0.5f // Top Of Screen
        )
    }

    private fun calculateScreenMatrices() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        sceneView.cameraNode.view.viewport.width = screenWidth
        sceneView.cameraNode.view.viewport.height = screenHeight
        sceneView.cameraNode.updateProjection()
        if(sceneView.childNodes.isNotEmpty()) {
            updateNodePositions()
        }
    }
}