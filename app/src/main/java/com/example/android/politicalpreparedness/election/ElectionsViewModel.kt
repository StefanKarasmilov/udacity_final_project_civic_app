package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepositoryImpl
import kotlinx.coroutines.launch

class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionsRepositoryImpl(database)

    val elections: LiveData<List<Election>> = repository.elections
    val storedElections: LiveData<List<Election>> = repository.storedElections

    init {
        getElections()
    }

    private fun getElections() {
        viewModelScope.launch { repository.getElections() }
    }

}