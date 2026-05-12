package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.presentation.ui.screen.TodoDetailScreen
import com.example.todolist.presentation.ui.screen.TodoListScreen
import com.example.todolist.presentation.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppNavGraph(
    viewModel: TodoViewModel,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.TodoList.route,
        modifier = modifier
    ) {
        composable(AppScreen.TodoList.route) {
            TodoListScreen(
                todos = viewModel.todos,
                windowWidthSizeClass = windowWidthSizeClass,
                onToggleTodo = viewModel::toggleTodo,
                onOpenDetails = { todoId ->
                    navController.navigate(AppScreen.TodoDetail.createRoute(todoId))
                }
            )
        }

        composable(
            route = AppScreen.TodoDetail.route,
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId")
            val todo = viewModel.todos.firstOrNull { it.id == todoId }
            if (todo != null) {
                TodoDetailScreen(todo = todo, onBack = { navController.popBackStack() })
            }
        }
    }
}
