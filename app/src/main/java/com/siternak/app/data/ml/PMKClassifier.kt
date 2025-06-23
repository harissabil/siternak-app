package com.siternak.app.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.get
import androidx.core.graphics.scale
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer

class PMKClassifier(private val context: Context) {

    private var interpreter: Interpreter? = null

    fun loadModel(modelPath: String) {
        try {
            val model = loadModelFile(modelPath)
            interpreter = Interpreter(model)
            Log.d("PMK", "Model loaded successfully")
        } catch (e: Exception) {
            Log.e("PMK", "Error loading model: ${e.message}")
        }
    }

    fun classifyImage(bitmap: Bitmap): Pair<String, Float> {
        val input = preprocessImage(bitmap)
        val output = Array(1) { FloatArray(PMKConfig.NUM_CLASSES) }

        interpreter?.run(input, output)

        val probabilities = output[0]
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
        val confidence = probabilities[maxIndex]
        val className = PMKConfig.CLASS_NAMES[maxIndex]

        return Pair(className, confidence)
    }

    private fun preprocessImage(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val resized = bitmap.scale(PMKConfig.MODEL_INPUT_SIZE, PMKConfig.MODEL_INPUT_SIZE)

        val input = Array(1) {
            Array(PMKConfig.MODEL_INPUT_SIZE) {
                Array(PMKConfig.MODEL_INPUT_SIZE) {
                    FloatArray(3)
                }
            }
        }

        for (y in 0 until PMKConfig.MODEL_INPUT_SIZE) {
            for (x in 0 until PMKConfig.MODEL_INPUT_SIZE) {
                val pixel = resized[x, y]

                // Normalize RGB values
                val r =
                    ((pixel shr 16 and 0xFF) / 255.0f - PMKConfig.MEAN_R) / PMKConfig.STD_R
                val g =
                    ((pixel shr 8 and 0xFF) / 255.0f - PMKConfig.MEAN_G) / PMKConfig.STD_G
                val b = ((pixel and 0xFF) / 255.0f - PMKConfig.MEAN_B) / PMKConfig.STD_B

                input[0][y][x][0] = r
                input[0][y][x][1] = g
                input[0][y][x][2] = b
            }
        }

        return input
    }

    private fun loadModelFile(modelPath: String): ByteBuffer {
//        val fileDescriptor = context.assets.openFd(modelPath)
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        return FileUtil.loadMappedFile(context, modelPath)
    }
}