package com.pvcorazza.activityresultexample.ui.main

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import com.pvcorazza.activityresultexample.R
import com.pvcorazza.activityresultexample.databinding.FragmentExamplePermissionBinding

class ExamplePermissionFragment : Fragment() {

    companion object {
        fun newInstance() = ExamplePermissionFragment()
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS
        private const val READ_CALENDAR_PERMISSION = Manifest.permission.READ_CALENDAR
    }

    private lateinit var binding: FragmentExamplePermissionBinding

    /* Registra o callback de resultado com a função que será executada após a ação do usuário
     * no Dialog de permissões do sistema.
     * O método registerForActivityResult() deve ser chamado antes
     * da criação do Fragment (aqui na inicialização, no onAttach() ou no onCreate(),
     * caso contrário receberemos uma IllegalStateException()
     */
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Após a permissão garantida, podemos chamar o método para iniciar a câmera
                startCamera()
            }
        }
    private val requestContactsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (!permissions.values.contains(false)) {
                showData()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentExamplePermissionBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btPermission.setOnClickListener {
            checkCameraPermissions()
        }
        binding.btMultiplePermission.setOnClickListener {
            checkDataPermissions()
        }
    }


    private fun checkCameraPermissions() {
        val hasCameraPermission = checkPermissionGranted(CAMERA_PERMISSION)
        when {
            // Verifica se a permissão foi concedida, nesse caso podemos iniciar a câmera
            hasCameraPermission -> startCamera()
            /* Esse método retorna true quando o usuário negou a pemissão.
             * A recomendação, nesse caso, é exibir uma UI explicando
             * o motivo da solicitação
             */
            shouldShowRequestPermissionRationale(CAMERA_PERMISSION) -> showMessage()
            /* Chamamos o método launch da instância de ActivityResultLauncher
             * que criamos anteriormente para exibir o Dialog de permissões do sistema
             */
            else -> requestCameraPermissionLauncher.launch(CAMERA_PERMISSION)
        }
    }

    private fun checkDataPermissions() {
        val hasContactPermission = checkPermissionGranted(READ_CONTACTS_PERMISSION)
        val hasCalendarPermission = checkPermissionGranted(READ_CALENDAR_PERMISSION)
        when {
            // Verifica se ambas as permissões foram concedidas, nesse caso podemos exibir os dados
            hasContactPermission and hasCalendarPermission -> showData()
            /* Esse método retorna true quando o usuário negou a pemissão.
             * A recomendação, nesse caso, é exibir uma UI explicando
             * o motivo da solicitação
             */
            shouldShowRequestPermissionRationale(READ_CONTACTS_PERMISSION) ||
                    shouldShowRequestPermissionRationale(READ_CALENDAR_PERMISSION)
            -> showMessage()
            /* Chamamos o método launch da instância de ActivityResultLauncher
             * que criamos anteriormente para exibir o Dialog de permissões do sistema
             */
            else -> requestContactsPermissionLauncher.launch(
                arrayOf(
                    READ_CONTACTS_PERMISSION,
                    READ_CALENDAR_PERMISSION
                )
            )
        }
    }

    private fun showMessage() {
        /* TODO: Implementar um Snackbar ou Dialog com um botão possibilitando acessar as
         * configurações do app para modificar as permissões
         */
        Toast.makeText(
            requireContext(),
            getString(R.string.permission_rationale),
            Toast.LENGTH_SHORT
        ).show()

    }

    private fun startCamera() {
        // TODO: Implementar lógica de acesso a câmera
        Toast.makeText(requireContext(), getString(R.string.camera_permission), Toast.LENGTH_SHORT)
            .show()
    }


    private fun checkPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PERMISSION_GRANTED


    private fun showData() {
        // TODO: Implementar lógica para exibição dos dados recebidos
        Toast.makeText(
            requireContext(),
            getString(R.string.contacts_calendar_permission),
            Toast.LENGTH_SHORT
        ).show()
    }
}