package com.example.todolist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.data.local.TodoJsonDataSource
import com.example.todolist.data.repository.TodoRepositoryImpl
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.domain.usecase.GetTodosUseCase
import com.example.todolist.domain.usecase.ToggleTodoUseCase
import com.example.todolist.presentation.viewmodel.TodoViewModel

object Creator {

    private fun provideRepository(context: Context): TodoRepository {
        return TodoRepositoryImpl(TodoJsonDataSource(context))
    }

    fun provideTodoViewModelFactory(context: Context): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = provideRepository(context)
                return TodoViewModel(
                    GetTodosUseCase(repository),
                    ToggleTodoUseCase(repository)
                ) as T
            }
        }
    }
}
