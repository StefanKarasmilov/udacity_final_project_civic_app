package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ElectionsRepository {
    suspend fun getElections()
    suspend fun getVoterInfoById(electionId: Int, address: String)
    suspend fun insertElection(election: Election)
    suspend fun deleteElectionById(electionId: Int)
}

class ElectionsRepositoryImpl(
        private val database: ElectionDatabase
) : ElectionsRepository {

    val elections: LiveData<List<Election>> = database.electionDao.getAllElections()
    val storedElections: LiveData<List<Election>> = database.electionDao.getStoredElections()
    val voterInfo = MutableLiveData<VoterInfoResponse>()


    override suspend fun getElections() {
        try {
            withContext(Dispatchers.IO) {
                val electionsFromApi = CivicsApi.retrofitService.getElectionsAsync().await()
                val oldElections = elections.value
                database.electionDao.insert(electionsFromApi.elections)
                oldElections?.let {
                    database.electionDao.insert(oldElections)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getVoterInfoById(electionId: Int, address: String) {
        try {
            withContext(Dispatchers.IO) {
                val voterInfoFromApi = CivicsApi.retrofitService.getVoterInfoAsync(electionId, address).await()
                voterInfo.postValue(voterInfoFromApi)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun insertElection(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.insert(election)
        }
    }

    override suspend fun deleteElectionById(electionId: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.deleteElectionById(electionId)
        }
    }

}