package ru.tinted_knight.sberbanksms.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity

//class AgentViewModel(application: Application, id : Int) : AndroidViewModel(application) {
class AgentViewModel(application: Application, id : Int) : SberViewModel(application) {

    val agent: LiveData<AgentEntity> = repo.daoAgents().getById(id)

    val alias: LiveData<String> = repo.daoAlias().getByAgentId(id)

}