package com.example.todolist

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.domain.usecase.GetTodosUseCase
import com.example.todolist.domain.usecase.ToggleTodoUseCase
import com.example.todolist.navigation.AppNavGraph
import com.example.todolist.presentation.viewmodel.TodoViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TodoUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: TodoViewModel

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

    @Before
    fun setup() {
        val repository = FakeTodoRepository()
        viewModel = TodoViewModel(
            getTodosUseCase = GetTodosUseCase(repository),
            toggleTodoUseCase = ToggleTodoUseCase(repository) 
        )

        composeRule.setContent {
            AppNavGraph(
                viewModel = viewModel,
                windowWidthSizeClass = WindowWidthSizeClass.Compact
            )
        }
    }

    @Test
    fun list_showsAllThreeTasks() {
        composeRule.onNodeWithTag("todo_row_1").assertExists()
        composeRule.onNodeWithTag("todo_row_2").assertExists()
        composeRule.onNodeWithTag("todo_row_3").assertExists()
    }

    @Test
    fun checkbox_togglesStatus() {
        composeRule.onNodeWithTag("todo_checkbox_1").performClick()

        composeRule.runOnIdle {
            val item = viewModel.todos.first { it.id == 1 }
            assert(item.isCompleted)
        }
    }

    @Test
    fun navigation_listToDetailToList() {
        composeRule.onNodeWithTag("todo_row_1").performClick()
        composeRule.onNodeWithTag("todo_detail_screen").assertExists()

        composeRule.onNodeWithTag("back_button").performClick()
        composeRule.onNodeWithTag("todo_list").assertExists()
    }
}
