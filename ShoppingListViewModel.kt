package com.axionlabs.shoppinglist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.axionlabs.shoppinglist.data.ShoppingDatabase
import com.axionlabs.shoppinglist.data.ShoppingItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ShoppingDatabase.getDatabase(application).shoppingDao()

    val items = dao.getAllItems().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addItem(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            dao.insert(ShoppingItem(name = name.trim()))
        }
    }

    fun toggleItem(item: ShoppingItem) {
        viewModelScope.launch {
            dao.update(item.copy(isBought = !item.isBought))
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }
}
