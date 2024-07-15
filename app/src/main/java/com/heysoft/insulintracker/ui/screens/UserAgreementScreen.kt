package com.heysoft.insulintracker

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.heysoft.insulintracker.viewmodel.SharedViewModel

@Composable
fun UserAgreementScreen(
    sharedViewModel: SharedViewModel = viewModel(),
    navController: NavHostController,
    onAgreementAccepted: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val isAccepted = sharedViewModel.isAgreementAccepted.value ?: false

    LaunchedEffect(isAccepted) {
        if (isAccepted) {
            onAgreementAccepted()
            navController.navigate("carbsCountScreen") {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    if (!isAccepted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.user_agreement_part1_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part1_text),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.user_agreement_part2_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part2_text1),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part2_text2),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.user_agreement_part3_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part3_text1),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part3_text2),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.user_agreement_part4_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part4_text1),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part4_text2),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.user_agreement_part5_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part5_text1),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(id = R.string.user_agreement_part5_text2),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        activity?.finish()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Не принимать")
                }
                Button(
                    onClick = {
                        sharedViewModel.acceptAgreement()
                        navController.navigate("carbsCountScreen") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        onAgreementAccepted()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Принять")
                }
            }
        }
    }
}
