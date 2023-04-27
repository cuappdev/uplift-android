package com.cornellappdev.uplift

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.preferences.preferencesDataStore
import com.cornellappdev.uplift.models.DatastoreRepository
import com.cornellappdev.uplift.ui.MainNavigationWrapper
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.util.PREFERENCES_NAME

// Singleton
lateinit var datastoreRepository: DatastoreRepository

class MainActivity : ComponentActivity() {
    val Context.dataStore by preferencesDataStore(
        name = PREFERENCES_NAME
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datastoreRepository = DatastoreRepository(dataStore)

        setContent {
            UpliftTheme {
                MainNavigationWrapper()
            }
        }
    }
}