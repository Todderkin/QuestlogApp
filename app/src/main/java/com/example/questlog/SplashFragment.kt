package com.example.questlog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class SplashFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Через 1.5 секунды решаем, куда идти: сразу в игру или на вход
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isAdded) return@postDelayed
            val prefs = requireActivity().getSharedPreferences("quest_prefs", Context.MODE_PRIVATE)
            val loggedIn = prefs.getBoolean("is_logged_in", false)
            if (loggedIn) {
                findNavController().navigate(R.id.action_splashFragment_to_dashboardFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 1500)
    }
}