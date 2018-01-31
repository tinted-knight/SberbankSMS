package ru.tinted_knight.sberbanksms.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import ru.tinted_knight.sberbanksms.dao.AppDatabase
import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity
import ru.tinted_knight.sberbanksms.dao.entities.AliasEntity

class AgentCommonViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: AppDatabase = AppDatabase.getInstance(application)

    val agentList: LiveData<List<AgentEntity>> = repo.daoAgents().all

    fun agent(id: Int): LiveData<AgentEntity> = repo.daoAgents().getById(id)

    fun alias(id: Int): LiveData<String> = repo.daoAlias().getByAgentId(id)

    fun aliasList(): LiveData<List<AliasEntity>> = repo.daoAlias().all

    fun createAlias(alias: String) {
        val aliasEntity = AliasEntity()
        aliasEntity.alias = alias
        repo.daoAlias().insert(aliasEntity)
    }

    fun connectToAgent(aliasId: Int, agentId: Int) = repo.daoAgents().update(agentId, aliasId.toLong())

    var aliasId = -1

    fun getFullAliasByAgentId(id: Int) : String {
        val aliasEntity = repo.daoAlias().getAliasEntityByAgentId(id)
        aliasId = aliasEntity._id
        return aliasEntity.alias
    }

    fun updateAlias(alias : String) {
        val aliasEntity = AliasEntity()
        aliasEntity.alias = alias
        aliasEntity._id = aliasId

        repo.daoAlias().update(aliasEntity)
    }
}