package com.lab4.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lab4.data.entity.SubjectEntity

/**
 * SubjectDao - interface of communication with `subjects` table
 * - marked with @Dao annotation (Data Access Object)
 * - contains custom functions-mappers for management data in table
 */
@Dao
interface SubjectDao {
    // function for fetching all subjects from table
    @Query("SELECT * FROM subjects")
    suspend fun getAllSubjects(): List<SubjectEntity>

    // function for fetching single Subject by id
    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun getSubjectById(id: Int): SubjectEntity?

    // function for adding new value Subject to table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubject(subjectEntity: SubjectEntity)
}