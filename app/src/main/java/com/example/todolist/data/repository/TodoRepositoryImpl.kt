package com.example.todolist.data.repository

import com.example.todolist.data.local.TodoJsonDataSource
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository

class TodoRepositoryImpl(
    private val dataSource: TodoJsonDataSource
) : TodoRepository {

    private var cache: MutableList<TodoItem>? = null

    override suspend fun getTodos(): List<TodoItem> {
        if (cache == null) {
            cache = dataSource.getTodos().map { dto ->
                TodoItem(
                    id = dto.id,
                    title = dto.title,
                    description = dto.description,
                    isCompleted = dto.isCompleted
                )
            }.toMutableList()
        }
        return cache!!.toList()
    }

    override suspend fun toggleTodo(id: Int) {
        if (cache == null) getTodos()
        cache = cache?.map { item ->
            if (item.id == id) item.copy(isCompleted = !item.isCompleted) else item
        }?.toMutableList()
    }
}
