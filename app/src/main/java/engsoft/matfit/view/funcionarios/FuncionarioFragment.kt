package engsoft.matfit.view.funcionarios

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import engsoft.matfit.R
import engsoft.matfit.databinding.FragmentFuncionarioBinding
import engsoft.matfit.listener.OnFuncionarioListener
import engsoft.matfit.view.funcionarios.adapter.FuncionarioAdapter
import engsoft.matfit.view.viewmodel.FuncionarioViewModel

class FuncionarioFragment : Fragment() {

    private var _binding: FragmentFuncionarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FuncionarioViewModel
    private val adapter = FuncionarioAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFuncionarioBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FuncionarioViewModel::class.java]

        binding.recyclerListFuncionario.layoutManager = LinearLayoutManager(context)
        binding.recyclerListFuncionario.adapter = adapter

        val listener = object : OnFuncionarioListener {
            override fun onUpdate(cpf: String) {
                viewModel.buscarFuncionario(cpf)
                viewModel.buscarFuncionario.observe(viewLifecycleOwner) { funcionario ->
                    Log.i("info_onUpdateFuncionario", "Bem sucedido -> $funcionario")
                    if (funcionario != null) {
                        val intent = Intent(context, UpdateFuncionarioActivity::class.java)
                        intent.putExtra("cpf", funcionario.cpf)
                        intent.putExtra("nome", funcionario.nome)
                        intent.putExtra("funcao", funcionario.funcao)
                        intent.putExtra("cargaHoraria", funcionario.cargaHoraria)
                        startActivity(intent)
                    } else {
                        Log.i("info_onUpdateFuncionario", "Erro de execução -> $funcionario")
                        toast("Funcionário não encontrado")
                    }
                }
            }

            override fun onDelete(cpf: String) {
                AlertDialog.Builder(requireContext()).setTitle(getString(R.string.deleteAluno))
                    .setMessage(getString(R.string.textConfirmationDelete))
                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        viewModel.deletarFuncionario(
                            cpf
                        )
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, which -> null }
                    .create()
                    .show()
            }
        }

        adapter.attachListener(listener)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(context, AddFuncionarioActivity::class.java))
        }

        viewModel.listarFuncionarios()

        observadores()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listarFuncionarios()
    }

    override fun onResume() {
        super.onResume()
        viewModel.listarFuncionarios()
    }

    private fun observadores() {
        viewModel.listarFuncionario.observe(viewLifecycleOwner) {
            adapter.updateFuncionario(it)
        }
        viewModel.deletarFuncionario.observe(viewLifecycleOwner) {
            if (!it) {
                toast(getString(R.string.textSuccessDeleteFuncionario))
                viewModel.listarFuncionarios()
            } else toast(getString(R.string.textFailureDeleteFuncionario))
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}