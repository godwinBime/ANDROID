package com.example.restaurantsapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RestaurantScreen(){
    RestaurantItem()
}

@Composable
private fun RestaurantIcon(icon: ImageVector, modifier: Modifier){
    Image(imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier.padding(8.dp))
}

@Composable
private fun RestaurantDetails(modifier: Modifier){
//    Column(modifier = modifier){
//        Text(text = "Alfredo's dishes",
//            style = MaterialTheme.typography.h6)
//        CompositionLocalProvider (
//            LocalContentAlpha provides ContentenAlpha.meduim){
//
//        }
//    }
}

@Composable
fun RestaurantItem(){
    Card(modifier = Modifier.padding(8.dp)){
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)){
            RestaurantIcon(
                Icons.Filled.Place,
                Modifier.weight(0.15f))
            RestaurantDetails(Modifier.weight(0.85f))
        }
    }
}