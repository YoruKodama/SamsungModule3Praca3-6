package com.example.todolist.navigation

sealed class AppScreen(val route: String) {
    data object TodoList : AppScreen("todo_list")
    data object TodoDetail : AppScreen("todo_detail/{todoId}") {
        fun createRoute(todoId: Int) = "todo_detail/$todoId"
    }
}
