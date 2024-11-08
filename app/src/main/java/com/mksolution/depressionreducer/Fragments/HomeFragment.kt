package com.mksolution.depressionreducer.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mksolution.depressionreducer.*
import com.mksolution.depressionreducer.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.goBreathEqual.setOnClickListener {
            startActivity(Intent(requireContext(),EqualBreathActivity::class.java))
        }

        binding.goBreathBox.setOnClickListener {
            startActivity(Intent(requireContext(),BoxBrathingActivity::class.java))
        }

        binding.goBreathRelux.setOnClickListener {
            startActivity(Intent(requireContext(),AdvanceBreathingActivity::class.java))
        }

        binding.tvGoMusic.setOnClickListener {
            startActivity(Intent(requireContext(),MusicActivity::class.java))
        }

        binding.tvGoArticle.setOnClickListener {
            startActivity(Intent(requireContext(),ArticleActivity::class.java))
        }
        binding.goMotivation.setOnClickListener {
            startActivity(Intent(requireContext(),QuoteActivity::class.java))
        }
        binding.goNote.setOnClickListener {
            startActivity(Intent(requireContext(),EmossionActivity::class.java))
        }

        return binding.root
    }

}