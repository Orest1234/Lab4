package com.lab4.ui.screens.subjectDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab4.data.db.DatabaseStorage
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity
import kotlinx.coroutines.launch

@Composable
fun SubjectDetailsScreen(id: Int) {
    val context = LocalContext.current
    val db = DatabaseStorage.getDatabase(context)

    val subjectState = remember { mutableStateOf<SubjectEntity?>(null) }
    val subjectLabsState = remember { mutableStateOf<List<SubjectLabEntity>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        subjectState.value = db.subjectsDao.getSubjectById(id)
        subjectLabsState.value = db.subjectLabsDao.getSubjectLabsBySubjectId(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Предмет",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = subjectState.value?.title ?: "",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        Text(
            text = "Лабораторні роботи",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        ) {
            items(subjectLabsState.value) { lab ->
                LabItem(
                    lab = lab,
                    onStatusChange = { updatedLab ->
                        coroutineScope.launch {
                            db.subjectLabsDao.updateSubjectLab(updatedLab)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LabItem(lab: SubjectLabEntity, onStatusChange: (SubjectLabEntity) -> Unit) {
    var isInProgress by remember { mutableStateOf(lab.inProgress) }
    var isCompleted by remember { mutableStateOf(lab.isCompleted) }
    var comment by remember { mutableStateOf(lab.comment ?: "") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isInProgress, isCompleted, comment) {
        onStatusChange(
            lab.copy(
                inProgress = isInProgress,
                isCompleted = isCompleted,
                comment = comment
            )
        )
    }

    Surface(
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = lab.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Text(
                text = lab.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Checkbox(
                    checked = isInProgress,
                    onCheckedChange = { checked ->
                        isInProgress = checked
                        if (checked) isCompleted = false
                    }
                )
                Text(text = "В прогресі", style = MaterialTheme.typography.bodySmall)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { checked ->
                        isCompleted = checked
                        if (checked) isInProgress = false
                    }
                )
                Text(text = "Завершено", style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = comment,
                onValueChange = { newComment -> comment = newComment },
                label = { Text("Коментар до завдання") },
                placeholder = { Text("Напишіть ваш коментар тут...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Button(
                onClick = { focusManager.clearFocus() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зберегти коментар")
            }
        }
    }
}
