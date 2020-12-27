package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    private lateinit var viewModel: ElectionsViewModel
    private lateinit var viewModelFactory: ElectionsViewModelFactory

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentElectionBinding.inflate(
                inflater,
                container,
                false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        viewModelFactory = ElectionsViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)
        binding.viewModel = viewModel

        setUpUpcomingElectionsAdapter()
        setUpStoredElectionsAdapter()
        manageDataMessages()

        return binding.root
    }

    private fun setUpUpcomingElectionsAdapter() {
        val adapter = ElectionListAdapter(ElectionListener {
            findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it))
        })
        binding.upcomingElectionsRecycler.adapter = adapter
    }

    private fun setUpStoredElectionsAdapter() {
        val adapter = ElectionListAdapter(ElectionListener {
            findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it))
        })

        binding.savedElectionsRecycler.adapter = adapter
    }

    private fun manageDataMessages() {
        viewModel.elections.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.noRemoteDataText.visibility = View.VISIBLE
            } else {
                binding.noRemoteDataText.visibility = View.GONE
            }
        })

        viewModel.storedElections.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.noLocalDataText.visibility = View.VISIBLE
            } else {
                binding.noLocalDataText.visibility = View.GONE
            }
        })
    }

}