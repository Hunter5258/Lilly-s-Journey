package com.lillyjourney.ui.navigation

sealed class Screen(val route: String, val label: String, val icon: String) {
    data object Onboarding : Screen("onboarding", "", "")
    data object Dashboard : Screen("dashboard", "Home", "home")
    data object Pregnancy : Screen("pregnancy", "Pregnancy", "pregnant_woman")
    data object Reminders : Screen("reminders", "Reminders", "notifications")
    data object Providers : Screen("providers", "Providers", "local_hospital")
    data object Safety : Screen("safety", "Safety", "warning")
    data object Reports : Screen("reports", "Reports", "description")
    data object Settings : Screen("settings", "Settings", "settings")

    data object Medicines : Screen("records/medicines", "Medicines", "medication")
    data object Prescriptions : Screen("records/prescriptions", "Prescriptions", "receipt_long")
    data object Tests : Screen("records/tests", "Tests", "science")
    data object Vitals : Screen("records/vitals", "Vitals", "monitor_heart")
    data object Vaccinations : Screen("records/vaccinations", "Vaccinations", "vaccines")
    data object Appointments : Screen("records/appointments", "Appointments", "calendar_month")
    data object Symptoms : Screen("records/symptoms", "Symptoms", "sick")
}
