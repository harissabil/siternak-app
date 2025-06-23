package com.siternak.app.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.siternak.app.core.theme.SiTernakTheme
import com.siternak.app.databinding.FragmentResultBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultViewModel by viewModel()
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setResult(args.result)

        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                SiTernakTheme {
                    val result by viewModel.uiState.collectAsState()
                    ResultScreen(
                        result = result,
                        onSave = {
                            Toast.makeText(context, "Hasil disimpan!", Toast.LENGTH_SHORT).show()
                        }
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