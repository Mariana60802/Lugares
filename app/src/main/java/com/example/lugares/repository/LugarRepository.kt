package com.example.lugares.repository


import androidx.lifecycle.MutableLiveData
import com.example.lugares.data.LugarDao
import com.example.lugares.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    // Se implementan las funciones de la interface

    //Se crea un objeto que contiene el arrayList de los registros de la tabla lugar ... cubiertos por LiveData
    val getAllData: MutableLiveData<List<Lugar>> = lugarDao.getAllData()

    //Define la funcion para insertar o actualizar un lugar en la coleccion misLugares
     fun saveLugar(lugar:Lugar)
    {
        lugarDao.saveLugar(lugar)
    }

    //Define la funcion para eliminar un lugar en la tabla LUGAR
     fun deleteLugar(lugar:Lugar)
    {
        lugarDao.deleteLugar(lugar)
    }


}