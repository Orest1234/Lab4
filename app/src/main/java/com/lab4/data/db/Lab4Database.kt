package com.lab4.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lab4.data.dao.SubjectDao
import com.lab4.data.dao.SubjectLabsDao
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Lab4Database - the main database class
 * - extends on RoomDatabase()
 * - marked with @Database annotation for generating communication interfaces
 * - in annotation are added all your entities (tables)
 * - includes abstract properties of all DAO interfaces for each entity (table)
 */
@Database(entities = [SubjectEntity::class, SubjectLabEntity::class], version = 1)
abstract class Lab4Database : RoomDatabase() {
    //DAO properties for each entity (table)
    // must be abstract (because Room will generate instances by itself)
    abstract val subjectsDao: SubjectDao
    abstract val subjectLabsDao: SubjectLabsDao
}

/**
 * DatabaseStorage - custom class where you initialize and store Lab4Database single instance
 *
 */
object DatabaseStorage {
    // ! Important - all operations with DB must be done from non-UI thread!
    // coroutineScope: CoroutineScope - is the scope which allows to run asynchronous operations
    // > we will learn it soon! For now just put it here
    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        },
    )

    // single instance of Lab4Database
    private var _database: Lab4Database? = null

    /**
        Function of initializing and getting Lab4Database instance
        - is invoked from place where DB should be used (from Compose screens)
        [context] - context from Compose screen to init DB
    */
    fun getDatabase(context: Context): Lab4Database {
        // if _database already contains Lab4Database instance, return this instance
        if (_database != null) return _database as Lab4Database
        // if not, create instance, preload some data and return this instance
        else {
            // creating Lab4Database instance by builder
            _database = Room.databaseBuilder(
                context,
                Lab4Database::class.java, "lab4Database"
            ).build()

            // preloading some data to DB
            preloadData()

            return _database as Lab4Database
        }
    }

    /**
        Function for preloading some initial data to DB
     */
    private fun preloadData() {
        // List of subjects
        val listOfSubject = listOf(
            SubjectEntity(id = 1, title = "Розробка мобільних додатків"),
            SubjectEntity(id = 2, title = "Адміністрування серверів та хмарних технологій"),
            SubjectEntity(id = 3, title = "Проєктування мережевих інфраструктур"),
            SubjectEntity(id = 4, title = "Інформаційна безпека в ІТ"),
        )
        // List of labs
        val listOfSubjectLabs = listOf(
            SubjectLabEntity(
                id = 1,
                subjectId = 1,
                title = "Вступ до Android",
                description = "Налаштування Android Studio та створення першого проєкту",
                comment = "Дедлайн: 15 листопада",
            ),
            SubjectLabEntity(
                id = 2,
                subjectId = 1,
                title = "Побудова UI в Android",
                description = "Розробка простого додатку для відстеження завдань",
                comment = "Виконано успішно",
                isCompleted = true
            ),
            SubjectLabEntity(
                id = 3,
                subjectId = 2,
                title = "Основи віртуалізації",
                description = "Налаштування віртуальної машини з використанням VirtualBox і Vagrant",
                comment = "Потрібно доопрацювання звіту",
                isCompleted = false
            ),
            SubjectLabEntity(
                id = 4,
                subjectId = 2,
                title = "Контейнеризація з Docker",
                description = "Створення Docker-контейнерів та налаштування базового середовища",
                comment = "Захист заплановано на 20 листопада",
                inProgress = true
            ),
            SubjectLabEntity(
                id = 5,
                subjectId = 3,
                title = "Аналіз вимог до мережі",
                description = "Складання технічного завдання для корпоративної мережі",
                comment = "Чекає перевірки",
                isCompleted = true
            ),
            SubjectLabEntity(
                id = 6,
                subjectId = 3,
                title = "Налаштування VLAN",
                description = "Практичне налаштування віртуальних локальних мереж",
                comment = "Захищено 10 листопада",
                isCompleted = true
            ),
            SubjectLabEntity(
                id = 7,
                subjectId = 4,
                title = "Безпека в мережах",
                description = "Налаштування Syslog, NTP та SSH на маршрутизаторах",
                comment = "Необхідно подати звіт до 25 листопада",
                isCompleted = false
            ),
            SubjectLabEntity(
                id = 8,
                subjectId = 4,
                title = "Презентація з безпеки",
                description = "Підготовка презентації про сучасні мережеві екрани",
                comment = "Здано на відмінно",
                isCompleted = true
            ),
        )


    // Request to add all Subjects from the list to DB
        listOfSubject.forEach { subject ->
            // coroutineScope.launch{...} - start small thread where you can make query to DB
            coroutineScope.launch {
                // INSERT query to add Subject (subjectsDao is used)
                _database?.subjectsDao?.addSubject(subject)
            }
        }
        // Request to add all Labs from the list to DB
        listOfSubjectLabs.forEach { lab ->
            coroutineScope.launch {
                // INSERT query to add Lab (subjectLabsDao is used)
                _database?.subjectLabsDao?.addSubjectLab(lab)
            }
        }
    }
}