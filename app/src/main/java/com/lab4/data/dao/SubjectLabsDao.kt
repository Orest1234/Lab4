package com.lab4.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lab4.data.entity.SubjectLabEntity

/**
 * SubjectLabsDao - interface of communication with `subjectsLabs` table
 * - marked with @Dao annotation (Data Access Object)
 * - contains custom functions-mappers for management data in table
 */
@Dao
interface SubjectLabsDao {
    // function for fetching all labs from table
    @Query("SELECT * FROM subjectsLabs")
    suspend fun getAllSubjectLabs(): List<SubjectLabEntity>

    // function for fetching all labs from table and filtering them by subjectId
    @Query("SELECT * FROM subjectsLabs WHERE subject_id = :subjectId")
    suspend fun getSubjectLabsBySubjectId(subjectId: Int): List<SubjectLabEntity>

    // function for fetching adding new value Lab to table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubjectLab(subjectLabEntity: SubjectLabEntity)

    @Update
    suspend fun updateSubjectLab(subjectLabEntity: SubjectLabEntity)
}