package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.repository.ElectionsRepositoryImpl
import kotlinx.coroutines.launch

class VoterInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionsRepositoryImpl(database)

    val voterInfo = repository.voterInfo

    private val _isFollowed = MutableLiveData<Boolean>(false)
    val isFollowed: LiveData<Boolean>
        get() = _isFollowed

    private val _onUrlOpen = MutableLiveData<String>()
    val onUrlOpen: LiveData<String>
        get() = _onUrlOpen

    fun isElectionFollowed(isFollowed: Boolean) {
        _isFollowed.value = isFollowed
    }

    fun getVoterInfo(electionId: Int, address: String) {
        viewModelScope.launch {
            repository.getVoterInfoById(electionId, address)
        }
    }

    fun saveElection(election: Election) {
        viewModelScope.launch {
            repository.insertElection(election)
        }
        election.isStored = !election.isStored
        _isFollowed.value = !_isFollowed.value!!
    }

    fun onUrlOpenStart(url: String) {
        _onUrlOpen.value = url
    }

    fun onUrlOpenEnd() {
        _onUrlOpen.value = null
    }

}