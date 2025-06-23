package com.siternak.app.ui.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.siternak.app.core.theme.SiTernakTheme
import com.siternak.app.databinding.FragmentPmkQuestionnaireBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PmkQuestionnaireFragment : Fragment() {

    private var _binding: FragmentPmkQuestionnaireBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PmkQuestionnaireViewModel by viewModel()
    private val args: PmkQuestionnaireFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel.setInitialScanResult(args.scanResult)
        _binding = FragmentPmkQuestionnaireBinding.inflate(inflater, container, false)
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
                    val state by viewModel.uiState.collectAsStateWithLifecycle()

                    // Observer untuk navigasi
                    LaunchedEffect(state.navigateToResult) {
                        if (state.navigateToResult && state.finalResult != null) {
                            val action = PmkQuestionnaireFragmentDirections.actionNavigationQuestionnaireToNavigationResult(state.finalResult!!)
                            findNavController().navigate(action)
                            viewModel.onResultNavigationHandled()
                        }
                    }

                    PmkQuestionnaireScreen(
                        state = state,
                        onEvent = viewModel::onEvent
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