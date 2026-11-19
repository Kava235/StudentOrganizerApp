package com.example.studentorganizer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Course(
    val name: String,
    val code: String,
    val instructor: String,
    val progress: Float
)

data class Assignment(
    val id: Int,
    val title: String,
    val course: String,
    val dueDate: String,
    val priority: String,
    val description: String = "",
    val reminder: Boolean = false,
    val isCompleted: Boolean = false
)

enum class AssignmentFilter(val label: String) {
    All("All"),
    Pending("Pending"),
    Completed("Completed")
}

/** Hoisted app-wide preferences used by the Settings screen. */
class AppSettings {
    var darkMode by mutableStateOf(false)
    var pushNotifications by mutableStateOf(true)
    var defaultStartScreen by mutableStateOf("Dashboard")
}

val priorityOptions = listOf("Low", "Medium", "High")
val courseOptions = listOf("CS101", "MATH201", "ENG102", "PHY110", "MAADK")

val sampleStudentName = "Kedar S Vastrad"
val sampleStudentId = "PES1UG25CS822"
val sampleMajor = "Computer Science & Engineering"

val sampleCourses = listOf(
    Course("Modern Android App Dev", "MAADK", "Prof. Shridevi A Sawant", 0.80f),
    Course("Data Structures", "CS101", "Prof. R. Menon", 0.65f),
    Course("Linear Algebra", "MATH201", "Prof. A. Iyer", 0.50f),
    Course("Technical English", "ENG102", "Prof. S. Rao", 0.90f)
)

/** 9 assignments: 7 pending + 2 completed (matches the 'Pending Tasks (7)' stat). */
val sampleAssignments = listOf(
    Assignment(1, "Compose Navigation Lab", "MAADK", "20 Nov 2026", "High", "Build a 5 screen app."),
    Assignment(2, "Linked List Problems", "CS101", "21 Nov 2026", "High", "Solve 10 problems."),
    Assignment(3, "Eigenvalues Worksheet", "MATH201", "23 Nov 2026", "Medium", "Worksheet 6."),
    Assignment(4, "Technical Report Draft", "ENG102", "25 Nov 2026", "Low", "First draft of the report."),
    Assignment(5, "Binary Tree Traversals", "CS101", "27 Nov 2026", "Medium", "Implement in Kotlin."),
    Assignment(6, "Matrix Decomposition", "MATH201", "29 Nov 2026", "Low", "LU and QR."),
    Assignment(7, "Presentation Slides", "ENG102", "30 Nov 2026", "Medium", "10 slides."),
    Assignment(8, "State Hoisting Quiz", "MAADK", "10 Nov 2026", "Low", "Done.", isCompleted = true),
    Assignment(9, "Array Basics", "CS101", "05 Nov 2026", "Low", "Done.", isCompleted = true)
)
