package com.cornellappdev.uplift.ui.components.gymdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun GymEquipmentSection() {
    Column(){
        Text(
            text = "Equipment",
            fontFamily = montserratFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PRIMARY_BLACK
        )
        categories.forEach() { category ->
            Text(
                text = category.name,
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PRIMARY_BLACK
            )
            category.subcategories?.forEach() { subcategory ->
                Text(
                    text = subcategory,
                    fontFamily = montserratFamily,
                    fontSize = 14.sp,
                    color = PRIMARY_BLACK
                )
            }
        }
    }
}

// in gym details viewmodel write function to take in list of equipment and return filtered list based on category
val categories = listOf(
    Category(
        name = "Legs",
        subcategories = listOf("Quads", "Hamstrings", "Calves", "Glutes")
    ),
    Category(
        name = "Arms",
        subcategories = listOf("Biceps", "Triceps")
    ),
    Category(
        name = "Chest",
    ),
    Category(
        name = "Back",
    ),
    Category(
        name = "Shoulders",
    ),
    Category(
        name = "Abdominals",
    ),
    Category(
        name = "Cardio",
    ),
    Category(
        name = "Miscellaneous",
    )
)

data class Category(
    val name: String,
    val subcategories: List<String>? = null
)