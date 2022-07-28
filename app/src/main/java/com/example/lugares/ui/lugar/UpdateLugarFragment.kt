package com.example.lugares.ui.lugar

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lugares.model.Lugar
import com.example.lugares.viewmodel.LugarViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import com.bumptech.glide.Glide
import com.lugares.R
import com.lugares.databinding.FragmentUpdateLugarBinding

class UpdateLugarFragment : Fragment()
{
    //Defno un argumento
    private val args by navArgs<UpdateLugarFragmentArgs>()
    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    //Para escuchar el audio grabado
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding = FragmentUpdateLugarBinding.inflate(inflater,container,false)

        //Se coloca la info del objeto lugar que pasaron
        binding.etNombre.setText(args.lugar.nombre)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etWeb.setText(args.lugar.web)

        //Se agrega la funcion para agregar un lugar
        binding.btActualizar.setOnClickListener{
            updateLugar();
        }

        binding.tvAltura.text=args.lugar.altura.toString()
        binding.tvLatitud.text=args.lugar.latitud.toString()
        binding.tvLongitud.text=args.lugar.longitud.toString()

        binding.btEmail.setOnClickListener{escribirCorreo()}
        binding.btPhone.setOnClickListener{llamarLugar()}
        binding.btWeb.setOnClickListener{verWeb()}
        binding.btBorrar.setOnClickListener{deleteLugar()}

        //Para inicializar y activar el boton de play si hay ruta de audio
        if(args.lugar.rutaAudio?.isNotEmpty()==true){
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.lugar.rutaAudio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled = true
            binding.btPlay.setOnClickListener{mediaPlayer.start()}
        }else{
            binding.btPlay.isEnabled = false
        }


        //Si hay ruta de imagen... La dibujo
        if(args.lugar.rutaImagen?.isNotEmpty()==true){
            Glide.with(requireContext())
                .load(args.lugar.rutaImagen)
                .fitCenter()
                .into(binding.imagen)
        }

        /*
        binding.btWhatsapp.setOnClickListener{enviarWhatsApp()}

        binding.btLocation.setOnClickListener{verMapa()}
        */


        //Se indica que en esta pantalla se agrega una opción de menú
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun verWeb() {
        //Recuper el URL del lugar
        val recurso = binding.etWeb.text.toString()
        if (recurso.isNotEmpty()){
            //Se abre el sitio web
            val rutina = Intent(Intent.ACTION_VIEW, Uri.parse("http://$recurso"))

            startActivity(rutina)  //Levanta el browser y se ve el sitio web
        }else{
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun llamarLugar() {
        //Se recupera el numero de telefono del lugar
        val recurso = binding.etTelefono.text.toString()
        if (recurso.isNotEmpty()){
            //Se activa el correo
            val rutina = Intent(Intent.ACTION_CALL)
            rutina.data = Uri.parse("tel:$recurso")
            if(requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED){
                //Se solicitan los permisos ... porque no están otorgados
                requireActivity()
                    .requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105)
            }else{
                requireActivity().startActivity(rutina) // Hace la llamada..
            }
            startActivity(rutina)  //Levanta el correo y lo presenta para modificar y enviar
        }else{
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun escribirCorreo() {
        //Se recupera el correo del lugar
        val recurso = binding.etCorreo.text.toString()
        if (recurso.isNotEmpty()){
            //Se activa el correo
            val rutina = Intent(Intent.ACTION_SEND)
            rutina.type="message/rfc822"
            rutina.putExtra(Intent.EXTRA_EMAIL, arrayOf(recurso))
            rutina.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.msg_saludos) + " " + binding.etNombre.text)
            rutina.putExtra(Intent.EXTRA_TEXT,
            getString(R.string.msg_mensaje_correo))
            startActivity(rutina)  //Levanta el correo y lo presenta para modificar y enviar
        }else{
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }

    }

    //Aca se genera el menu con el icono de borrar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    //Aca se programa que si se detecta un click en el ícono borrar .. haga algo..
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Pregunto si se dio click en el ícono de borrar
        if(item.itemId==R.id.menu_delete){
            //Hace algo si se dio click
            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteLugar() {
        val consulta = AlertDialog.Builder(requireContext())

        consulta.setTitle(R.string.delete)
        consulta.setMessage(getString(R.string.seguroBorrar) + " ${args.lugar.nombre}?")

        //Acciones a ejecutar si respondo Yes
        consulta.setPositiveButton(getString(R.string.si)){_,_ ->
            //Borrarmos el lugar...
            lugarViewModel.deleteLugar(args.lugar)
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }
        consulta.setNegativeButton(getString(R.string.no)){_,_ ->

        }
        consulta.create().show()

    }

    private fun updateLugar() {
        val nombre =binding.etNombre.text.toString()
        val correo =binding.etCorreo.text.toString()
        val telefono =binding.etTelefono.text.toString()
        val web =binding.etWeb.text.toString()
        if(nombre.isNotEmpty()){
            val lugar = Lugar(args.lugar.id,nombre,correo,telefono,web,0.0,0.0,0.0,"","")
            lugarViewModel.saveLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.lugarUpdated),Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        } else{
            Toast.makeText(requireContext(),getString(R.string.noData),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}