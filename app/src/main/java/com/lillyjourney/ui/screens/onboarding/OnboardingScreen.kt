package com.lillyjourney.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted

@Composable
fun OnboardingScreen() {
    var step by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn())
                    .togetherWith(slideOutHorizontally(animationSpec = tween(200)) { -it } + fadeOut())
            },
            label = "onboarding_step",
        ) { currentStep ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                when (currentStep) {
                    0 -> WelcomeStep(onNext = { step = 1 })
                    1 -> PregnancyDetailsStep(onNext = { step = 2 })
                    2 -> SecuritySetupStep(onNext = { step = 3 })
                    3 -> ContactsStep(onNext = { step = 4 })
                    4 -> CompletionStep(onComplete = { step = 0 })
                }
            }
        }
    }
}

@Composable
private fun WelcomeStep(onNext: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Primary.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "\u2764\uFE0F", fontSize = 36.sp)
    }
    Spacer(Modifier.height(20.dp))
    Text(
        text = "Welcome to\nLilly's Journey",
        style = MaterialTheme.typography.displayMedium,
        textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = "Your pregnancy care companion",
        style = MaterialTheme.typography.bodyLarge,
        color = TextMuted,
    )
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
    ) {
        Text("Get Started", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PregnancyDetailsStep(onNext: () -> Unit) {
    Text(
        text = "Pregnancy Details",
        style = MaterialTheme.typography.headlineMedium,
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = "Set your due date or last menstrual period",
        style = MaterialTheme.typography.bodyMedium,
        color = TextMuted,
    )
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
    ) {
        Text("Continue", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SecuritySetupStep(onNext: () -> Unit) {
    Text(
        text = "Security",
        style = MaterialTheme.typography.headlineMedium,
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = "Protect your data with app lock",
        style = MaterialTheme.typography.bodyMedium,
        color = TextMuted,
    )
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
    ) {
        Text("Continue", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ContactsStep(onNext: () -> Unit) {
    Text(
        text = "Emergency Contact",
        style = MaterialTheme.typography.headlineMedium,
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = "Add an optional emergency contact",
        style = MaterialTheme.typography.bodyMedium,
        color = TextMuted,
    )
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
    ) {
        Text("Continue", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CompletionStep(onComplete: () -> Unit) {
    Text(
        text = "You're all set!",
        style = MaterialTheme.typography.displayMedium,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = "Start tracking your journey",
        style = MaterialTheme.typography.bodyLarge,
        color = TextMuted,
    )
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = onComplete,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
    ) {
        Text("Go to Dashboard", fontWeight = FontWeight.Bold)
    }
}
