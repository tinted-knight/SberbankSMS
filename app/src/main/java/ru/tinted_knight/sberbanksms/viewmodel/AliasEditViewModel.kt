package ru.tinted_knight.sberbanksms.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import ru.tinted_knight.sberbanksms.dao.AppDatabase
import ru.tinted_knight.sberbanksms.dao.entities.AliasEntity

class AliasEditViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppDatabase.getInstance(application)

    var aliasId = -1

    fun updateAlias(alias : String) {
        val aliasEntity = AliasEntity()
        aliasEntity.alias = alias
        aliasEntity._id = aliasId

        repo.daoAlias().update(aliasEntity)
    }

    fun getFullAliasByAgentId(id: Int) : String {
        val aliasEntity = repo.daoAlias().getAliasEntityByAgentId(id)
        aliasId = aliasEntity._id
        return aliasEntity.alias
    }

}