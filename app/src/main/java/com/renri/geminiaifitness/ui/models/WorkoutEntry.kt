package com.renri.geminiaifitness.ui.models

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutEntry(
    val dayOfWeek: String,
    val warmUp: List<String>,
    val mainWorkout: List<String>,
    val coolDown: List<String>
)