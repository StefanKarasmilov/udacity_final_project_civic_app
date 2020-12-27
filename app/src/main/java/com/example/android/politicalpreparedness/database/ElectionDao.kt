package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(elections: List<Election>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE is_stored = 1")
    fun getStoredElections(): LiveData<List<Election>>

    @Query("DELETE FROM election_table WHERE id = :electionId")
    fun deleteElectionById(electionId: Int)

}