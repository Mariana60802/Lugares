package com.example.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lugares.model.Lugar
import com.example.lugares.ui.lugar.LugarFragmentDirections
import com.lugares.databinding.LugarFilaBinding

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>(){

    // Una lista para almacenar la informacón de los lugares
    private var listaLugares = emptyList<Lugar>()

    inner class LugarViewHolder(private var itemBinding: LugarFilaBinding) :
    RecyclerView.ViewHolder(itemBinding.root){
        fun dibuja(lugar: Lugar){

            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvTelefono.text = lugar.telefono
            itemBinding.tvWeb.text = lugar.web

            Glide.with(itemBinding.root.context)
                .load(lugar.rutaImagen)
                .circleCrop()
                .into(itemBinding.imagen)

            itemBinding.vistaFila.setOnClickListener{
                val accion = LugarFragmentDirections
                    .actionNavLugarToUpdateLugarFragment(lugar)
                itemView.findNavController().navigate(accion)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return LugarViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar = listaLugares[position]
        holder.dibuja(lugar)
    }

    override fun getItemCount(): Int {
        return listaLugares.size
    }

    fun setData(lugares: List<Lugar>){
        this.listaLugares=lugares
        //La siguiente información redibuja toda la lista del reciclador
        notifyDataSetChanged()
    }
}