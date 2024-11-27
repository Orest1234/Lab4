package com.lab4.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lab4.ui.screens.subjectDetails.SubjectDetailsScreen
import com.lab4.ui.screens.subjectsList.SubjectsListScreen


const val SCREEN_SUBJECTS_LIST = "subjectsList"
const val SCREEN_SUBJECT_DETAILS = "subjectDetails"

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        navController = navController,
        startDestination = SCREEN_SUBJECTS_LIST
    ) {

        composable(
            route = SCREEN_SUBJECTS_LIST
        ) {
            SubjectsListScreen { id ->
                navController.navigate("$SCREEN_SUBJECT_DETAILS/$id")
            }
        }

        composable(
            route = "$SCREEN_SUBJECT_DETAILS/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                },
            )
        ) { backStack ->
            SubjectDetailsScreen(id = backStack.arguments?.getInt("id") ?: 0)
        }

    }
}