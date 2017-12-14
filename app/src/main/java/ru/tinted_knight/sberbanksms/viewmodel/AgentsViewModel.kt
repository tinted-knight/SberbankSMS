package ru.tinted_knight.sberbanksms.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import ru.tinted_knight.sberbanksms.dao.AppDatabase
import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity

class AgentsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: AppDatabase = AppDatabase.getInstance(application)

    val agents: LiveData<List<AgentEntity>> = repo.daoAgents().all

}