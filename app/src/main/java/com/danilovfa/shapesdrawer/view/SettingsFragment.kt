package com.danilovfa.shapesdrawer.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.danilovfa.shapesdrawer.databinding.FragmentSettingsBinding
import com.danilovfa.shapesdrawer.model.Serializer
import com.google.gson.JsonParser
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import java.io.File
import java.lang.Byte


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel
    private val serializer = Serializer()
    private val TAG = "Settings"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Settings"

        binding.toJson.setOnClickListener { onToJson(it) }
        binding.fromJson.setOnClickListener { onFromJson(it) }
        binding.toTxt.setOnClickListener { onToTxt(it) }
        binding.fromTxt.setOnClickListener { onFromTxt(it) }
        binding.toBin.setOnClickListener { onToBin(it) }
        binding.fromBin.setOnClickListener { onFromBin(it) }

    }

    private fun onToJson(view: View) {
        if (sharedViewModel.shapes == null) {
            Toast.makeText(requireContext(), "No shapes drawn!", Toast.LENGTH_SHORT).show()
            return
        }
        val json = serializer.toJson(sharedViewModel.shapes!!)
        Log.d(TAG, "onToJson: $json")
        try {
            val path = requireActivity().getExternalFilesDir(null)
            val folder = File(path, "saves")
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "save.json")
            file.writeText(json.toString())
        } catch (e: Exception) {
            Log.d(TAG, "onToJson: Error saving file (${e.message})")
        }
    }

    private fun onFromJson(view: View) {
        try {
            val path = requireActivity().getExternalFilesDir(null)
            val folder = File(path, "saves")
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "save.json")
            if (!file.exists()) {
                Toast.makeText(requireContext(), "No saved shapes!", Toast.LENGTH_SHORT).show()
                return
            }
            val str = file.readText()
            sharedViewModel.shapes = serializer.fromJson(Json.parseToJsonElement(str).jsonArray)
        } catch (e: Exception) {
            Log.d(TAG, "onFromJson: Error loading file (${e.message})")
        }
    }

    private fun onToTxt(view: View) {
        if (sharedViewModel.shapes == null) {
            Toast.makeText(requireContext(), "No shapes drawn!", Toast.LENGTH_SHORT).show()
            return
        }
        val txt = serializer.toTxt(sharedViewModel.shapes!!)
        Log.d(TAG, "onToTxt: $txt")
        try {
            val path = requireActivity().getExternalFilesDir(null)
            val folder = File(path, "saves")
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "save.txt")
            file.writeText(txt)
        } catch (e: Exception) {
            Log.d(TAG, "onToTxt: Error saving file (${e.message})")
        }
    }

    private fun onFromTxt(view: View) {
        try {
            val path = requireActivity().getExternalFilesDir(null)
            val folder = File(path, "saves")
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "save.txt")
            if (!file.exists()) {
                Toast.makeText(requireContext(), "No saved shapes!", Toast.LENGTH_SHORT).show()
                return
            }
            val str = file.readText()
            sharedViewModel.shapes = serializer.fromTxt(str)
        } catch (e: Exception) {
            Log.d(TAG, "onFromTxt: Error loading file (${e.message})")
        }
    }

    private fun onToBin(view: View) {
        if (sharedViewModel.shapes == null) {
            Toast.makeText(requireContext(), "No shapes drawn!", Toast.LENGTH_SHORT).show()
            return
        }
        val bin = serializer.toBin(sharedViewModel.shapes!!)
        Log.d(TAG, "onToBin: $bin")
        try {
            val path = requireActivity().getExternalFilesDir(null)
            val folder = File(path, "saves")
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "save.bin")
            val temp = bin.toMutableList()
            temp.forEachIndexed { index, byte ->
                temp[index] = (byte.toInt()-'a'.code).toByte()
            }
            file.writeBytes(temp.toByteArray())
        } catch (e: Exception) {
            Log.d(TAG, "onToBin: Error saving file (${e.message})")
        }
    }

    private fun onFromBin(view: View) {
        try {
            val path = requireActivity().getExternalFilesDir(null)
            val folder = File(path, "saves")
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "save.bin")
            if (!file.exists()) {
                Toast.makeText(requireContext(), "No saved shapes!", Toast.LENGTH_SHORT).show()
                return
            }
            val str = file.readBytes()
            val temp = str.toMutableList()
            temp.forEachIndexed { index, byte ->
                temp[index] = (byte.toInt()+'a'.code).toByte()
            }
            sharedViewModel.shapes = serializer.fromBin(temp.toByteArray())
        } catch (e: Exception) {
            Log.d(TAG, "onFromBin: Error loading file (${e.message})")
        }
    }
}