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
data class TeamMember(
    val name: String,
    val role: String,
    @DrawableRes val imageRes: Int
)

data class Team(
    val semester: String,
    val members: List<TeamMember>
)

/** TEAMS_LIST is a hardcoded mapping of relevant semesters and their teams **/
private val TEAMS_LIST: Map<String, List<TeamMember>> = mapOf(
    "Fall 2026" to listOf(
        TeamMember("Nina", "Pod Lead",  R.drawable.nina_img),
        TeamMember("Katie", "Design",  R.drawable.katie_img),
        TeamMember("Zoe", "Design",  R.drawable.zoe_img),
        TeamMember("Anatoli", "IOS",  R.drawable.anatoli_img),
        TeamMember("Kaylee", "IOS",  R.drawable.kaylee_img),
        TeamMember("Sandy", "IOS",  R.drawable.sandy_img),
        TeamMember("Tran", "Backend",  R.drawable.tran_img),
        TeamMember("Wyatt", "Backend",  R.drawable.wyatt_img),
        TeamMember("Melissa", "Android",  R.drawable.melissa_img),
        TeamMember("Connie", "Android",  R.drawable.connie_img),
        TeamMember("Cinnie", "Android",  R.drawable.cinnie_img),
        TeamMember("Wendy", "Marketing",  R.drawable.wendy_img)
    ),
    "Spring 2026" to listOf(
        TeamMember("Angela", "Pod Lead",  R.drawable.angela_img),
        TeamMember("Enzo", "APL",  R.drawable.enzo_img),
        TeamMember("Selena", "Design",  R.drawable.selena_img),
        TeamMember("Caitlyn", "IOS",  R.drawable.caitlyn_img),
        TeamMember("Jiwon", "IOS",  R.drawable.jiwon_img),
        TeamMember("Anatoli", "IOS",  R.drawable.anatoli_img),
        TeamMember("Sophie", "Backend",  R.drawable.sophie_img),
        TeamMember("Chimdi", "Backend",  R.drawable.chimdi_img),
        TeamMember("Yitbrek", "Backend",  R.drawable.yitbrek_img),
        TeamMember("Melissa", "Android",  R.drawable.melissa_img),
        TeamMember("Preston", "Android",  R.drawable.preston_img),
        TeamMember("Wendy", "Marketing",  R.drawable.wendy_img)
    ),
)

private const val ALL_SEMESTERS = "All Semesters"

private val SEMESTERS = listOf(ALL_SEMESTERS) + TEAMS_LIST.keys


data class AboutUiState(
    val semesters: List<String> = SEMESTERS,
    val selectedSemester: String = SEMESTERS.first(),
    val errorState: Boolean = false
) {
    val isAll: Boolean
        get() = selectedSemester == ALL_SEMESTERS

    val sections: List<Team>
        get() = if (isAll) {
            TEAMS_LIST.map { (semester, members) -> Team(semester, members) }
        } else {
            listOf(Team(selectedSemester, TEAMS_LIST[selectedSemester].orEmpty()))
        }
}

@HiltViewModel
class AboutViewModel @Inject constructor() :
    UpliftViewModel<AboutUiState>(AboutUiState()) {

    fun onSemesterSelected(semester: String) {
        applyMutation { copy(selectedSemester = semester) }
    }
}