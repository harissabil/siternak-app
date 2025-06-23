package com.siternak.app.ui.pmk_detector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.siternak.app.core.theme.SiTernakTheme
import com.siternak.app.databinding.FragmentPmkDetectorBinding
import com.siternak.app.ui.pmk_detector.component.ActionButtonsSection
import com.siternak.app.ui.pmk_detector.component.ArticleSection
import com.siternak.app.ui.pmk_detector.component.EmergencyContactsSection
import com.siternak.app.ui.pmk_detector.component.GreetingSection
import org.koin.androidx.viewmodel.ext.android.viewModel

class PmkDetectorFragment : Fragment() {

    private var _binding: FragmentPmkDetectorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PmkDetectorViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPmkDetectorBinding.inflate(inflater, container, false)
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
                    val userName = viewModel.userName.observeAsState()

                    PmkDetectorScreen(
                        name = userName.value ?: "Pengguna",
                        onScanClick = {
                            requireView().findNavController().navigate(
                                PmkDetectorFragmentDirections.actionNavigationPmkDetectorToNavigationQuestionnaire()
                            )
                        },
                        onRiwayatClick = {  },
                        onHotlineClick = {  },
                        onRSHewanClick = {  },
                        onArticleClick = {  }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PmkDetectorScreen(
    modifier: Modifier = Modifier,
    name: String,
    onScanClick: () -> Unit,
    onRiwayatClick: () -> Unit,
    onHotlineClick: () -> Unit,
    onRSHewanClick: () -> Unit,
    onArticleClick: (String) -> Unit = { /* Default no-op */ },
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "PMK Detector",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
            )
        },
        containerColor = Color(0xFFF5F5F5) // Light gray background for the whole screen
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            GreetingSection(name)
            Spacer(modifier = Modifier.height(24.dp))
            ActionButtonsSection(
                onScanClick = onScanClick,
                onRiwayatClick = onRiwayatClick
            )
            Spacer(modifier = Modifier.height(32.dp))
            EmergencyContactsSection(
                onHotlineClick = onHotlineClick,
                onRSHewanClick = onRSHewanClick
            )
            Spacer(modifier = Modifier.height(32.dp))
            ArticleSection()
        }
    }
}