package ru.tinted_knight.sberbanksms.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import ru.tinted_knight.sberbanksms.dao.AppDatabase
import ru.tinted_knight.sberbanksms.dao.entities.AliasEntity

class AliasCreateViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppDatabase.getInstance(application)

    fun createAlias(alias : String, agentId : Int) {
        val aliasEntity = AliasEntity()
        aliasEntity.alias = alias

        val aliasId = repo.daoAlias().insert(aliasEntity)
        repo.daoAgents().update(agentId, aliasId)
    }

//    fun getAliasByAgentId(agentId: Int) : LiveData<String> {
//        return repo.daoAlias().getByAgentId(agentId)
//    }
}