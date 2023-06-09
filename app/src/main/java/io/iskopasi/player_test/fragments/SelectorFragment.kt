package io.iskopasi.player_test.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.iskopasi.player_test.databinding.FragmentSelectorBinding

@AndroidEntryPoint
class SelectorFragment : Fragment() {
    private lateinit var binding: FragmentSelectorBinding

    private val requesterXml = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionCallback(isGranted, SelectorFragmentDirections.actionToXml())
    }

    private val requesterCompose = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionCallback(isGranted, SelectorFragmentDirections.actionToCompose())
    }

    private val requesterRx = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionCallback(isGranted, SelectorFragmentDirections.actionToRx())
    }

    private fun permissionCallback(isGranted: Boolean, action: NavDirections) = when {
        isGranted -> onGranted(action)
        !ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) -> onPermaDenied()

        else -> onDenied(action)
    }

    private fun getRequester(action: NavDirections) = when (action) {
        SelectorFragmentDirections.actionToXml() -> requesterXml
        SelectorFragmentDirections.actionToCompose() -> requesterCompose
        SelectorFragmentDirections.actionToRx() -> requesterRx
        else -> requesterXml
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectorBinding.inflate(inflater, container, false)

        binding.buttonXml.setOnClickListener {
            requestPermissionContract(SelectorFragmentDirections.actionToXml())
        }
        binding.buttonJetpack.setOnClickListener {
            Snackbar.make(binding.root, "Not implemented", Snackbar.LENGTH_LONG).show()
        }

        return binding.root
    }

    private fun onGranted(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun onDenied(action: NavDirections) {
        requestPermissionContract(action)
    }

    private fun onPermaDenied() {
        findNavController().navigate(SelectorFragmentDirections.actionToPermissionDenied())
    }

    private fun requestPermissionContract(action: NavDirections) {
        val requester = getRequester(action)

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            AlertDialog.Builder(requireActivity())
                .setTitle("We need file permission")
                .setMessage("Plz!")
                .setPositiveButton(
                    "OK"
                ) { dialog, which ->
                    requester.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    onPermaDenied()
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        } else {
            requester.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}