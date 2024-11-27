package com.lab4.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * SubjectEntity - the data class which represents the `subjects` table
 * - marked with annotation @Entity - for SQL tables
 * - contains @PrimaryKey field id - all objects in tables has unique primary keys
 */
@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: Int,
    val title: String
)