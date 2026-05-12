package com.example.todolist.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.presentation.ui.component.TodoRow
import com.example.todolist.presentation.viewmodel.CounterViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TodoListScreen(
    todos: List<TodoItem>,
    windowWidthSizeClass: WindowWidthSizeClass,
    onToggleTodo: (Int) -> Unit,
    onOpenDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            TodoListSinglePane(
                todos = todos,
                onToggleTodo = onToggleTodo,
                onOpenDetails = onOpenDetails,
                modifier = modifier
            )
        }

        WindowWidthSizeClass.Medium,
        WindowWidthSizeClass.Expanded -> {
            TodoListTwoPane(
                todos = todos,
                onToggleTodo = onToggleTodo,
                onOpenDetails = onOpenDetails,
                modifier = modifier
            )
        }

        else -> {
            TodoListSinglePane(
                todos = todos,
                onToggleTodo = onToggleTodo,
                onOpenDetails = onOpenDetails,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun CounterCard(modifier: Modifier = Modifier) {
    val counterViewModel: CounterViewModel = viewModel()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Счётчик: ${counterViewModel.count}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = counterViewModel::increment) {
                Text("+1")
            }
        }
    }
}

@Composable
private fun TodoListSinglePane(
    todos: List<TodoItem>,
    onToggleTodo: (Int) -> Unit,
    onOpenDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Todo List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        CounterCard()
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.testTag("todo_list")) {
            items(todos, key = { it.id }) { item ->
                TodoRow(item = item, onToggle = onToggleTodo, onOpenDetails = onOpenDetails)
            }
        }
    }
}

@Composable
private fun TodoListTwoPane(
    todos: List<TodoItem>,
    onToggleTodo: (Int) -> Unit,
    onOpenDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
        ) {
            Text(
                text = "Todo List",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            CounterCard()
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.testTag("todo_list")) {
                items(todos, key = { it.id }) { item ->
                    TodoRow(item = item, onToggle = onToggleTodo, onOpenDetails = onOpenDetails)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Выберите задачу в левой колонке",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
