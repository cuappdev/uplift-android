package com.cornellappdev.uplift.ui.components.goalsetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.uplift.R
import com.cornellappdev.uplift.ui.theme.AppTextStyles
import com.cornellappdev.uplift.ui.theme.UpliftTheme
import com.cornellappdev.uplift.util.PRIMARY_BLACK
import com.cornellappdev.uplift.util.PRIMARY_YELLOW
import com.cornellappdev.uplift.util.montserratFamily

@Composable
fun GoalSettingHeader(
    isOnboarding: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val daysPerWeekQuestion = stringResource(R.string.goals_days_per_week_question)
        if (!isOnboarding) {
            val setAPlan = stringResource(R.string.goals_set_a_plan)
            Text(
                buildAnnotatedString {
                    withStyle(AppTextStyles.HeadingBold.toSpanStyle()) {
                        append(setAPlan)
                    }
                    withStyle(AppTextStyles.HeadingSemibold.toSpanStyle()) {
                        append(daysPerWeekQuestion)
                    }
                },
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(end = 30.dp)
            )
        } else {
            Text(
                text = daysPerWeekQuestion,
                style = AppTextStyles.HeadingSemibold,
                color = PRIMARY_BLACK,
                modifier = Modifier.padding(end = 30.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(PRIMARY_YELLOW)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.goals_unchangeable_notice),
                fontFamily = montserratFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PRIMARY_BLACK
            )
        }
    }
}

@Preview(showBackground = true, name = "Onboarding")
@Composable
private fun GoalSettingHeaderOnboardingPreview() {
    UpliftTheme {
        GoalSettingHeader(isOnboarding = true)
    }
}

@Preview(showBackground = true, name = "My Goals")
@Composable
private fun GoalSettingHeaderPreview() {
    UpliftTheme {
        GoalSettingHeader(isOnboarding = false)
    }
}
