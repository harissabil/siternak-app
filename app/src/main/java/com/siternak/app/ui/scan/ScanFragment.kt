package com.siternak.app.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.siternak.app.core.theme.SiTernakTheme
import com.siternak.app.databinding.FragmentScanBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScanViewModel by viewModel()
    private val cameraViewModel: CameraPreviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                SiTernakTheme {
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val snackbarHostState = remember { SnackbarHostState() }

                    LaunchedEffect(key1 = Unit) {
                        viewModel.errorMessage.collectLatest { message ->
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(message = message)
                        }
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        ScanScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            viewModel = viewModel,
                            cameraPreviewViewModel = cameraViewModel,
                            lifecycleOwner = viewLifecycleOwner
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    cameraPreviewViewModel: CameraPreviewViewModel,
    viewModel: ScanViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    var currentStep by remember { mutableStateOf(ScanStep.MOUTH) }
    val scanSteps = ScanStep.entries.toTypedArray()
    val context = LocalContext.current

    val flashMode by cameraPreviewViewModel.flashMode.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White) // Latar belakang putih untuk keseluruhan layar
    ) {
        // Top Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White) // Latar belakang putih jika diperlukan terpisah
                .padding(top = 24.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Foto ${scanSteps.indexOf(currentStep) + 1}:",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = currentStep.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentStep.instruction,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }

        // Camera Preview Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), // Membuat preview mengisi sisa ruang
            contentAlignment = Alignment.Center
        ) {
            CameraPreviewScreen( // Menggunakan komponen CameraX yang sudah ada
                viewModel = cameraPreviewViewModel,
                lifecycleOwner = lifecycleOwner,
                modifier = Modifier.fillMaxSize()
            )
            // Viewfinder Border Overlay (seperti di gambar)
            ViewfinderBorderOverlay(modifier = Modifier.matchParentSize())
        }

        // Bottom Controls Section
        BottomControls(
            currentStep = currentStep,
            flashMode = flashMode,
            onPrevious = {
                val currentIndex = scanSteps.indexOf(currentStep)
                if (currentIndex > 0) {
                    currentStep = scanSteps[currentIndex - 1]
                }
            },
            onNext = {
                val currentIndex = scanSteps.indexOf(currentStep)
                if (currentIndex < scanSteps.size - 1) {
                    currentStep = scanSteps[currentIndex + 1]
                } else {
                    // TODO: Handle Selesai atau navigasi ke layar berikutnya
                    Toast.makeText(context, "Semua langkah selesai!", Toast.LENGTH_SHORT).show()
                }
            },
            onCapture = {
                cameraPreviewViewModel.captureImage(context) { file ->
                    viewModel.onSetImageFile(currentStep, file)
                    // Otomatis lanjut ke step berikutnya setelah capture jika diinginkan
                     val currentIndex = scanSteps.indexOf(currentStep)
                     if (currentIndex < scanSteps.size - 1) {
                         currentStep = scanSteps[currentIndex + 1]
                     } else {
                         Toast.makeText(context, "Semua langkah selesai!", Toast.LENGTH_SHORT).show()
                         viewModel.classifyTongue()
                     }
                }
            },
            onGallery = {
                Toast.makeText(context, "Buka Galeri (belum diimplementasi)", Toast.LENGTH_SHORT).show()
            },
            onToggleFlash = {
                cameraPreviewViewModel.toggleFlash()
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) // Rounded top corners
                .padding(16.dp)

        )
    }
}

@Composable
fun ViewfinderBorderOverlay(modifier: Modifier = Modifier) {
    // Ukuran dan ketebalan border bisa disesuaikan
    val cornerSize = 40.dp
    val strokeWidth = 6.dp
    val color = Color.White.copy(alpha = 0.8f)

    Box(modifier = modifier.padding(24.dp)) { // Padding agar border tidak terlalu mepet
        // Top-Left Corner
        Spacer(
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(cornerSize)
                .height(strokeWidth)
                .background(color)
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.TopStart)
                .height(cornerSize)
                .width(strokeWidth)
                .background(color)
        )

        // Top-Right Corner
        Spacer(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(cornerSize)
                .height(strokeWidth)
                .background(color)
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .height(cornerSize)
                .width(strokeWidth)
                .background(color)
        )

        // Bottom-Left Corner
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .width(cornerSize)
                .height(strokeWidth)
                .background(color)
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .height(cornerSize)
                .width(strokeWidth)
                .background(color)
        )

        // Bottom-Right Corner
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(cornerSize)
                .height(strokeWidth)
                .background(color)
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .height(cornerSize)
                .width(strokeWidth)
                .background(color)
        )
    }
}


@Composable
fun BottomControls(
    currentStep: ScanStep,
    flashMode: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onCapture: () -> Unit,
    onGallery: () -> Unit,
    onToggleFlash: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Navigation Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Padding horizontal untuk navigasi
                .padding(bottom = 16.dp), // Jarak antara navigasi dan tombol aksi
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val scanSteps = ScanStep.entries.toTypedArray()
            IconButton(
                onClick = onPrevious,
                enabled = scanSteps.indexOf(currentStep) > 0 // Disable jika step pertama
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Previous Step",
                    modifier = Modifier.size(28.dp),
                    tint = if (scanSteps.indexOf(currentStep) > 0) MaterialTheme.colorScheme.primary else Color.LightGray
                )
            }

            Text(
                text = currentStep.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            IconButton(
                onClick = onNext,
                enabled = scanSteps.indexOf(currentStep) < scanSteps.size - 1 // Disable jika step terakhir
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Next Step",
                    modifier = Modifier.size(28.dp),
                    tint = if (scanSteps.indexOf(currentStep) < scanSteps.size - 1) MaterialTheme.colorScheme.primary else Color.LightGray
                )
            }
        }

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly // Atau SpaceAround untuk jarak lebih
        ) {
            CircleIconButton(
                icon = Icons.Filled.PhotoLibrary,
                contentDescription = "Open Gallery",
                onClick = onGallery
            )

            CaptureButton(onClick = onCapture)

            CircleIconButton(
                icon = if (flashMode == ImageCapture.FLASH_MODE_ON) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                contentDescription = "Toggle Flash",
                onClick = onToggleFlash
            )
        }
    }
}

@Composable
fun CircleIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFFF0F0F0), // Warna abu-abu muda seperti di gambar
    iconColor: Color = Color.Black
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier.size(64.dp), // Ukuran tombol lingkaran
        contentPadding = PaddingValues(0.dp) // Hapus padding default agar ikon pas
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(28.dp) // Ukuran ikon di dalam tombol
        )
    }
}

@Composable
fun CaptureButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6E6E6)), // Warna luar
        border = BorderStroke(4.dp, Color.Red), // Border merah seperti di gambar
        modifier = Modifier.size(80.dp), // Ukuran tombol capture lebih besar
        contentPadding = PaddingValues(0.dp)
    ) {
        // Inner circle (optional, bisa juga langsung icon)
        Box(
            modifier = Modifier
                .size(60.dp) // Ukuran lingkaran dalam
                .background(Color.Red, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.CameraAlt, // Atau ikon record seperti di gambar
                contentDescription = "Capture Image",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}