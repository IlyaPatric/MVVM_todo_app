package com.example.mvvmtodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mvvmtodoapp.ui.add_edit_todo.AddEditTodoScreen
import com.example.mvvmtodoapp.ui.theme.MVVMTodoAppTheme
import com.example.mvvmtodoapp.ui.todo_list.TodoListScreen
import com.example.mvvmtodoapp.utils.Routes
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val systemUiController = rememberSystemUiController()
            val useDarkIcons = isSystemInDarkTheme()
            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = if (useDarkIcons) Color(0xFF121212) else Color.White,
                    darkIcons = !useDarkIcons
                )
                onDispose {}
            }

            MVVMTodoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.TODO_LIST
                    ) {
                        composable(route = Routes.TODO_LIST) {
                            TodoListScreen(onNavigate = {
                                navController.navigate(it.route)
                            })
                        }
                        composable(
                            route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                            arguments = listOf(
                                navArgument(name = "todoId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            AddEditTodoScreen(onPopBackStack = {
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}