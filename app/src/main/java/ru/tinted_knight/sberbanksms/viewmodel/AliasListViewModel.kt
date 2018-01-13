package ru.tinted_knight.sberbanksms.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import ru.tinted_knight.sberbanksms.dao.entities.AliasEntity

class AliasListViewModel(application: Application) : SberViewModel(application) {

    val aliasList: LiveData<List<AliasEntity>> = repo.daoAlias().all

    fun createAlias(alias : String) {
        val aliasEntity = AliasEntity()
        aliasEntity.alias = alias
        repo.daoAlias().insert(aliasEntity)
    }

    fun connectToAgent(aliasId: Int, agentId: Int) {
        repo.daoAgents().update(agentId, aliasId.toLong())
    }

}