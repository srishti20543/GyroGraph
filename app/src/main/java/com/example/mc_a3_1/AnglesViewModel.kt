package com.example.mc_a3_1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AnglesViewModel(private val anglesDao: AnglesDao) : ViewModel() {

    fun insert(angles: Angles) = viewModelScope.launch {
        anglesDao.upsertAngles(angles)
    }

    val angleEntries: Flow<List<Angles>> = anglesDao.getAllAngles()

}