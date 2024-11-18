package com.niderkvel.iskraapispring.forms

import com.niderkvel.iskraapispring.models.Subtask

data class TaskForm(
    val subtasks: List<Subtask>,
)
