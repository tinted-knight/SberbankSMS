package ru.tinted_knight.sberbanksms.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import ru.tinted_knight.sberbanksms.dao.AppDatabase
import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity

class AgentViewModel(application: Application, id : Int) : AndroidViewModel(application) {

    private val repo = AppDatabase.getInstance(application)

    val agent: LiveData<AgentEntity> = repo.daoAgents().getById(id)

}