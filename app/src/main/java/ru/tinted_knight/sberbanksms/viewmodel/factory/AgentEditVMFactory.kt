package ru.tinted_knight.sberbanksms.viewmodel.factory

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.tinted_knight.sberbanksms.viewmodel.AgentViewModel

class AgentEditVMFactory(
        private val application: Application,
        private val id: Int
    ) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AgentViewModel(application, id) as T
    }

}