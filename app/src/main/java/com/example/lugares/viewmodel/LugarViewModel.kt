package com.example.lugares.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.lugares.data.LugarDao
import com.example.lugares.model.Lugar
import com.example.lugares.repository.LugarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {

    val getAllData: MutableLiveData<List<Lugar>>

    //Esta es la manera de acceder al repositorio desde el viewModel
    private val repository: LugarRepository = LugarRepository(LugarDao())

    //Procede a inicializar los atributos anteriores de esta clase LugarViewModel
    init
    {
        getAllData = repository.getAllData
    }
        //Esta funcion de alto nivel llama al subproceso de I/O para grabar o actualizar un Lugar
        fun saveLugar(lugar: Lugar)
        {
           viewModelScope.launch(Dispatchers.IO)
           {
               repository.saveLugar(lugar)
           }
        }

        //Esta funcion de alto nivel llama al subproceso de I/O para Eliminar un registro Lugar
        fun deleteLugar(lugar: Lugar)
        {
            viewModelScope.launch(Dispatchers.IO)
            {
                repository.deleteLugar(lugar)
            }
        }


}