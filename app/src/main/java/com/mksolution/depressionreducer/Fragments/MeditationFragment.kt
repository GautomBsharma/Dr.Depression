package com.mksolution.depressionreducer.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mksolution.depressionreducer.MaditatiomtwoActivity
import com.mksolution.depressionreducer.MeditationThreeActivity
import com.mksolution.depressionreducer.R
import com.mksolution.depressionreducer.RelaxMeditationActivity
import com.mksolution.depressionreducer.databinding.FragmentMeditationBinding


class MeditationFragment : Fragment() {
    private lateinit var binding: FragmentMeditationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMeditationBinding.inflate(layoutInflater)

        binding.button.setOnClickListener {
            startActivity(Intent(requireContext(), RelaxMeditationActivity::class.java))
        }
        binding.button2.setOnClickListener {
            startActivity(Intent(requireContext(), MaditatiomtwoActivity::class.java))
        }
        binding.button3.setOnClickListener {
            startActivity(Intent(requireContext(), MeditationThreeActivity::class.java))
        }

        return binding.root
    }

}