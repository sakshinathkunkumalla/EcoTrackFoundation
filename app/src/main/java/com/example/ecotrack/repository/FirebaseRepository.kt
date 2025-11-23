package com.example.ecotrack.repository

import com.example.ecotrack.model.ActivityEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"

    // Add activity to Firestore
    suspend fun addActivity(activity: ActivityEntity) {
        db.collection("users")
            .document(userId)
            .collection("activities")
            .add(activity)
            .await()
    }

    // Fetch all activities from Firestore
    suspend fun getActivities(): List<ActivityEntity> {
        val snapshot = db.collection("users")
            .document(userId)
            .collection("activities")
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(ActivityEntity::class.java) }
    }
}