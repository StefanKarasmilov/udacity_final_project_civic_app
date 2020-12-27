package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(application: Application) : AndroidViewModel(application), Observable {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    @Bindable
    private val _address = MutableLiveData<Address>(Address("", "", "", "", ""))
    val address: LiveData<Address>
        get() = _address


    fun getRepresentatives() {
        if (address.value != null) {
            viewModelScope.launch {
                try {
                    val (offices, officials) = CivicsApi.retrofitService.getRepresentativesAsync(_address.value!!.toFormattedString()).await()
                    _representatives.postValue(offices.flatMap { office -> office.getRepresentatives(officials) })
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setAddress(address: Address) {
        _address.value = address
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RepresentativeViewModel(app) as T
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

}
