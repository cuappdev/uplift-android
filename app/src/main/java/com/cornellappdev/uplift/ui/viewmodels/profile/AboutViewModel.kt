package com.cornellappdev.uplift.ui.viewmodels.profile

import androidx.annotation.DrawableRes
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.ViewModel
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.viewmodels.UpliftViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject
/** SEMESTERS is a hardcoded list of relevant semesters **/
private val SEMESTERS = listOf("Fall 2026", "Spring 2026")

data class TeamMember(
    val name: String,
    val role: String,
    @DrawableRes val imageRes: Int
)

/** ALL_SEMESTERS_TEAMS is a hardcoded mapping of relevant semesters and their teams **/
private val ALL_SEMESTERS_TEAMS: Map<String, List<TeamMember>> = mapOf(
    "Fall 2026" to listOf(
        TeamMember("Nina", "Pod Lead",  R.drawable.ic_appdev),
        TeamMember("Katie", "Design",  R.drawable.ic_appdev),
        TeamMember("Zoe", "Design",  R.drawable.ic_appdev),
        TeamMember("Anatoli", "IOS",  R.drawable.ic_appdev),
        TeamMember("Kaylee", "IOS",  R.drawable.ic_appdev),
        TeamMember("Sandy", "IOS",  R.drawable.ic_appdev),
        TeamMember("Tran", "Backend",  R.drawable.ic_appdev),
        TeamMember("Wyatt", "Backend",  R.drawable.ic_appdev),
        TeamMember("Melissa", "Android",  R.drawable.ic_appdev),
        TeamMember("Connie", "Android",  R.drawable.ic_appdev),
        TeamMember("Cinnie", "Android",  R.drawable.ic_appdev),
        TeamMember("Wendy", "Marketing",  R.drawable.ic_appdev)
    ),
    "Spring 2026" to listOf(
        TeamMember("Angela", "Pod Lead",  R.drawable.ic_appdev),
        TeamMember("Enzo", "APL",  R.drawable.ic_appdev),
        TeamMember("Selena", "Design",  R.drawable.ic_appdev),
        TeamMember("Caitlyn", "IOS",  R.drawable.ic_appdev),
        TeamMember("Jiwon", "IOS",  R.drawable.ic_appdev),
        TeamMember("Anatoli", "IOS",  R.drawable.ic_appdev),
        TeamMember("Sophie", "Backend",  R.drawable.ic_appdev),
        TeamMember("Chimdi", "Backend",  R.drawable.ic_appdev),
        TeamMember("Yitbrek", "Backend",  R.drawable.ic_appdev),
        TeamMember("Melissa", "Android",  R.drawable.ic_appdev),
        TeamMember("Preston", "Android",  R.drawable.ic_appdev),
        TeamMember("Wendy", "Marketing",  R.drawable.ic_appdev)
    ),
)


data class AboutUiState(
    val semesters: List<String> = SEMESTERS, // list of all semesters
    var selectedSemester: String = SEMESTERS.first(), // the currently selected semester
    val errorState: Boolean = false
) {
    val members: List<TeamMember> // gets all members of the selected semester
        get() = ALL_SEMESTERS_TEAMS[selectedSemester] ?: emptyList()
}

@HiltViewModel
class AboutViewModel @Inject constructor() :
    UpliftViewModel<AboutUiState>(AboutUiState()) {

    fun onSemesterSelected(semester: String) {
        applyMutation { copy(selectedSemester = semester) }
    }
}