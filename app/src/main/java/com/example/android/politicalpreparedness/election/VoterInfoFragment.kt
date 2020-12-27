package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val args: VoterInfoFragmentArgs by navArgs()

    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var factory: VoterInfoViewModelFactory

    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentVoterInfoBinding.inflate(
                inflater,
                container,
                false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        factory = VoterInfoViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(this, factory).get(VoterInfoViewModel::class.java)

        binding.election = args.election
        binding.viewModel = viewModel

        setUpViewModel()

        return binding.root
    }

    private fun setUpViewModel() {
        val electionId = args.election.id
        val state = args.election.division.state

        viewModel.isElectionFollowed(args.election.isStored)
        viewModel.getVoterInfo(electionId, state)

        viewModel.onUrlOpen.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
                viewModel.onUrlOpenEnd()
            }
        })
    }

}