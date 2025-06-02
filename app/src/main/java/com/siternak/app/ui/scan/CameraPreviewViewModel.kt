package com.siternak.app.ui.scan

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.Surface
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class CameraPreviewViewModel : ViewModel() {
    // Used to set up a link between the Camera and your UI.
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null
    private var cameraControl: CameraControl? = null
    private var processCameraProvider: ProcessCameraProvider? = null
    private var lifecycleOwner: LifecycleOwner? = null
    private var appContext: Context? = null

    // Keep track of current camera selection
    private val _cameraSelector = MutableStateFlow(CameraSelector.DEFAULT_BACK_CAMERA)

    private var imageCapture =
        ImageCapture.Builder().setJpegQuality(30)
            .setTargetRotation(Surface.ROTATION_0)
            .setFlashMode(ImageCapture.FLASH_MODE_OFF) // Initial flash mode
            .build()

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
            surfaceMeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                newSurfaceRequest.resolution.width.toFloat(),
                newSurfaceRequest.resolution.height.toFloat()
            )
        }
    }

    private val _flashMode = MutableStateFlow(ImageCapture.FLASH_MODE_OFF)
    val flashMode: StateFlow<Int> = _flashMode.asStateFlow()

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        this.appContext = appContext
        this.lifecycleOwner = lifecycleOwner
        processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        bindCamera()

        // Cancellation signals we're done with the camera
        try {
            awaitCancellation()
        } finally {
            processCameraProvider?.unbindAll()
            cameraControl = null
            this.appContext = null
            this.lifecycleOwner = null
            this.processCameraProvider = null
        }
    }

    private fun bindCamera() {
        val provider = processCameraProvider ?: return
        val owner = lifecycleOwner ?: return
        val imageCapture = imageCapture ?: return

        // Unbind all use cases before rebinding
        provider.unbindAll()

        // Bind camera with current selector
        val camera = provider.bindToLifecycle(
            owner, _cameraSelector.value, cameraPreviewUseCase, imageCapture
        )
        cameraControl = camera.cameraControl
        // After binding, ensure the torch state matches the desired flash mode
        // This is important if the flash was on, camera flipped, and then flipped back.
        cameraControl?.enableTorch(_flashMode.value == ImageCapture.FLASH_MODE_ON)
    }

    fun tapToFocus(tapCoords: Offset) {
        val point = surfaceMeteringPointFactory?.createPoint(tapCoords.x, tapCoords.y)
        if (point != null) {
            val meteringAction = FocusMeteringAction.Builder(point).build()
            cameraControl?.startFocusAndMetering(meteringAction)
        }
    }

    /**
     * Toggles between front and back cameras
     */
    fun flipCamera() {
        viewModelScope.launch {
            val newCameraSelector =
                if (_cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }

            _cameraSelector.value = newCameraSelector
            bindCamera()
        }
    }

    /**
     * Toggles the camera flash between on and off
     */
    fun toggleFlash() {
        viewModelScope.launch {
            // Check if the current camera has a flash unit.
            // This requires cameraInfo, which is available after binding.
            // For simplicity, we'll assume the primary camera has flash.
            // If cameraControl is null, it means camera is not yet bound or failed.
            if (cameraControl == null) {
                Log.w("CameraPreviewViewModel", "CameraControl not available to toggle flash.")
                return@launch
            }
            // Check if the camera actually has a flash unit
            // val hasFlash = cameraControl?.cameraInfo?.hasFlashUnit() == true
            // if (!hasFlash) {
            //    Log.i("CameraPreviewViewModel", "Current camera does not have a flash unit.")
            //    // Optionally, update UI to disable flash button
            //    return@launch
            // }


            val newFlashMode = if (_flashMode.value == ImageCapture.FLASH_MODE_OFF) {
                ImageCapture.FLASH_MODE_ON
            } else {
                ImageCapture.FLASH_MODE_OFF
            }

            _flashMode.value = newFlashMode

            // 1. Control the torch for the PREVIEW
            // This gives immediate visual feedback to the user.
            cameraControl?.enableTorch(newFlashMode == ImageCapture.FLASH_MODE_ON)

            // 2. Update ImageCapture for the ACTUAL CAPTURE
            // Re-create ImageCapture with the new flash mode.
            // This ensures that when takePicture() is called, it uses the correct flash setting.
            imageCapture = ImageCapture.Builder()
                .setFlashMode(newFlashMode)
                .setJpegQuality(30) // Maintain other settings
                .setTargetRotation(Surface.ROTATION_0) // Maintain other settings
                .build()

            // 3. Rebind use cases
            // While changing ImageCapture settings like flash mode often requires
            // unbinding and rebinding the use case, enableTorch is a CameraControl feature
            // and typically doesn't require a full rebind for the torch itself to work.
            // However, since we are replacing the imageCapture instance, we MUST rebind.
            bindCamera()
        }
    }

    fun captureImage(
        context: Context,
        onImageCapturedSuccess: (file: File) -> Unit,
    ) {
        val name = "${System.currentTimeMillis()}.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BISBI")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .setMetadata(ImageCapture.Metadata())
            .build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val file = outputFileResults.savedUri?.let { uri ->
                        File(getRealPathFromURI(context, uri))
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        rotateBitmap(file!!)
                        withContext(Dispatchers.Main) {
                            onImageCapturedSuccess(file)
                        }
                    }
                    Log.d(
                        "CameraPreviewViewModel",
                        "Image saved successfully: ${file?.absolutePath}"
                    )
                }

                suspend fun rotateBitmap(file: File) = withContext(Dispatchers.IO) {
                    val sourceBitmap =
                        MediaStore.Images.Media.getBitmap(context.contentResolver, file.toUri())

                    val exif = ExifInterface(file.inputStream())
                    val rotation =
                        exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL
                        )
                    val rotationInDegrees = when (rotation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270
                        ExifInterface.ORIENTATION_TRANSVERSE -> -90
                        ExifInterface.ORIENTATION_TRANSPOSE -> -270
                        else -> 0
                    }
                    val matrix = Matrix().apply {
                        if (rotation != 0) preRotate(rotationInDegrees.toFloat())
                    }

                    val rotatedBitmap =
                        Bitmap.createBitmap(
                            sourceBitmap,
                            0,
                            0,
                            sourceBitmap.width,
                            sourceBitmap.height,
                            matrix,
                            true
                        )

                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))

                    sourceBitmap.recycle()
                    rotatedBitmap.recycle()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(
                        "CameraPreviewViewModel",
                        "Image capture failed: ${exception.message}",
                        exception
                    )
                }

            })
    }
}

fun getRealPathFromURI(context: Context, contentUri: Uri?): String {
    var cursor: Cursor? = null
    try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    } finally {
        cursor?.close()
    }
}