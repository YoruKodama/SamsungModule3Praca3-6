package com.example.todolist

import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.domain.usecase.GetTodosUseCase
import com.example.todolist.domain.usecase.ToggleTodoUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TodoUseCasesTest {

    private class FakeTodoRepository : TodoRepository {
        private val items = mutableListOf(
            TodoItem(1, "Купить молоко", "2 литра, обезжиренное", false),
            TodoItem(2, "Позвонить маме", "Спросить про выходные", true),
            TodoItem(3, "Сделать ДЗ по Android", "Clean Architecture + Compose", false)
        )

        override suspend fun getTodos(): List<TodoItem> = items.toList()

        override suspend fun toggleTodo(id: Int) {
            val index = items.indexOfFirst { it.id == id }
            if (index >= 0) {
                val item = items[index]
                items[index] = item.copy(isCompleted = !item.isCompleted)
            }
        }
    }

    @Test
    fun getTodosUseCase_returnsThreeItems() = runTest {
        val repository = FakeTodoRepository()
        val useCase = GetTodosUseCase(repository)

        val result = useCase()

        assertEquals(3, result.size)
    }

    @Test
    fun toggleTodoUseCase_changesIsCompleted() = runTest {
        val repository = FakeTodoRepository()
        val getTodosUseCase = GetTodosUseCase(repository)
        val toggleTodoUseCase = ToggleTodoUseCase(repository)

        val before = getTodosUseCase().first { it.id == 1 }
        toggleTodoUseCase(1)
        val after = getTodosUseCase().first { it.id == 1 }

        assertFalse(before.isCompleted)
        assertTrue(after.isCompleted)
    }
}
