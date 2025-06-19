package com.example.weatherappdm2

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherappdm2.HomePage
import com.example.weatherappdm2.ListPage
import com.example.weatherappdm2.MapPage

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage() }
        composable<Route.List> { ListPage() }
        composable<Route.Map> { MapPage() }
    }
}