package com.atilsamancioglu.artbookcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.atilsamancioglu.artbookcompose.model.Art
import com.atilsamancioglu.artbookcompose.roomdb.ArtDao
import com.atilsamancioglu.artbookcompose.roomdb.ArtDatabase
import com.atilsamancioglu.artbookcompose.screens.AddArtScreen
import com.atilsamancioglu.artbookcompose.screens.ArtList
import com.atilsamancioglu.artbookcompose.screens.DetailScreen
import com.atilsamancioglu.artbookcompose.ui.theme.ArtBookComposeTheme
import com.atilsamancioglu.artbookcompose.viewmodels.ArtViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: ArtViewModel by viewModels<ArtViewModel>()

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
        val navController = rememberNavController()
        ArtBookComposeTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = "list_screen"
                    ) {
                        composable("list_screen") {
                            val artList = produceState<List<Art>>(
                                initialValue = emptyList<Art>()
                                )
                             {
                                value = viewModel.getArtList()
                            }.value

                            ArtList(arts = artList, navController = navController)
                        }

                        composable("add_art_screen") {
                            val coroutineScope = rememberCoroutineScope()  // Create a coroutine scope

                            AddArtScreen(saveFunction = { art ->
                                coroutineScope.launch {
                                    viewModel.saveArt(art)  // Call the suspend function within a coroutine
                                    navController.navigate(
                                        "list_screen"
                                    )
                                }

                            })

                        }

                        composable(
                            "details_screen/{artId}",
                            arguments = listOf(
                                navArgument("artId") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val artIdString = remember {
                                it.arguments?.getString("artId")
                            }

                            val chosenArt = produceState<Art?>(
                                initialValue = Art(
                                    "", "", "", ByteArray(
                                        1
                                    )
                                )
                            ) {
                                value = viewModel.getArt(artIdString?.toIntOrNull() ?: 1)
                            }.value

                            DetailScreen(
                                art = chosenArt
                            )

                        }
                    }


                }
            }
        }
    }
}
}



