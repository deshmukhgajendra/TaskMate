package com.example.taskmate.userInterface

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmate.R
import com.example.taskmate.model.Task
import com.example.taskmate.ui.theme.OutFitFontFamily
import com.example.taskmate.ui.theme.TaskMateTheme
import com.example.taskmate.utils.isOverdue
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission(this)
        }
        enableEdgeToEdge()
        setContent {
            TaskMateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    app(innerPadding)
                }
            }
        }
    }
}

// getting runtime permissions from user
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun requestNotificationPermission(context: Context) {
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            1001
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun app(paddingValues: PaddingValues,viewModel: ViewModel= hiltViewModel()){

    LaunchedEffect(Unit) {
       // viewModel.insertTask()
        //viewModel.getAllTasks()
        viewModel.fetchAllPriorityTasks()
    }

    val highPriorityTasks by viewModel.highTasks.collectAsState()
    val mudiumPriorityTasks by viewModel.mediumTasks.collectAsState()
    val lowPriorityTasks by viewModel.lowTasks.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomSheetState= rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {true}
    )

    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val menuItemData = listOf("High Priority", "Medium Priority", "Low Priority")
    var selectedPriority by remember { mutableStateOf("") }

    val selectedTask = viewModel.selectedTask

    var highExpanded by remember { mutableStateOf(true) }
    var mediumExpanded by remember { mutableStateOf(false) }
    var lowExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(selectedTask) {
        selectedTask?.let {
            title = it.title
            description = it.description
            selectedDate = it.dueDate
            selectedPriority = it.priority
        }
    }

    // to select task date
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState()
            DatePicker(state = datePickerState)

            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    selectedDate = formatter.format(Date(millis))
                }
            }
        }
    }
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 0.dp)
            ) {

                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth().focusable()
                )

                Spacer(modifier = Modifier.height(4.dp))

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Description") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth().focusable()
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_date_range_24filled),
                                contentDescription = "Select Date"
                            )
                        }

                        Box {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_star_border_24),
                                    contentDescription = "Select Priority"
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                menuItemData.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            selectedPriority = item
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            if (selectedTask != null) {
                                val updatedTask = selectedTask.copy(
                                    title = title,
                                    description = description,
                                    dueDate = selectedDate,
                                    priority = selectedPriority
                                )
                                viewModel.updateTask(updatedTask)
                            } else {
                                val newTask = Task(
                                    title = title,
                                    description = description,
                                    dueDate = selectedDate,
                                    priority = selectedPriority,
                                    isCompleted = false
                                )
                                viewModel.insertTask(newTask)
                            }
                            isBottomSheetVisible = false
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_check_circle_24),
                            contentDescription = "Save"
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), // 👈 Add this

        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "TaskMate",
                        style = TextStyle(
                            fontFamily = OutFitFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 35.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = " "
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search buton"
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Sort"
                        )
                    }
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "Filter"
                        )
                    }
                },
                scrollBehavior = scrollBehavior


            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {

                    isBottomSheetVisible = true
                },
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add,"Icon button")
            }
        }
    ) {innerPadding ->

        LazyColumn (
            modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ){

            item {
                PrioritySection(
                    title = "High Priority",
                    tasks = highPriorityTasks,
                    isExpanded = highExpanded,
                    onToggleExpand = { highExpanded = !highExpanded },
                    onDelete = { viewModel.deleteTask(it) },
                    onToggleComplete = { viewModel.updateTask(it) },
                    onEdit = {
                        viewModel.onEditClick(it)
                        isBottomSheetVisible = true
                    }
                )
            }

            item {
                PrioritySection(
                    title = "Medium Priority",
                    tasks = mudiumPriorityTasks,
                    isExpanded = mediumExpanded,
                    onToggleExpand = { mediumExpanded = !mediumExpanded },
                    onDelete = { viewModel.deleteTask(it) },
                    onToggleComplete = { viewModel.updateTask(it) },
                    onEdit = {
                        viewModel.onEditClick(it)
                        isBottomSheetVisible = true
                    }
                )
            }

            item {
                PrioritySection(
                    title = "Low Priority",
                    tasks = lowPriorityTasks,
                    isExpanded = lowExpanded,
                    onToggleExpand = { lowExpanded = !lowExpanded },
                    onDelete = { viewModel.deleteTask(it) },
                    onToggleComplete = { viewModel.updateTask(it) },
                    onEdit = {
                        viewModel.onEditClick(it)
                        isBottomSheetVisible = true
                    }
                )
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissTask(
    task: Task,
    onDelete: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onEdit: (Task) -> Unit,
    paddingValues: PaddingValues
) {
    val isOverdue = task.isOverdue()

    val threshold = with(LocalDensity.current) { 100.dp.toPx() }

    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { threshold }
    )


    LaunchedEffect(dismissState.currentValue, dismissState.targetValue) {
        when {
            dismissState.currentValue == SwipeToDismissBoxValue.EndToStart &&
                    dismissState.targetValue == SwipeToDismissBoxValue.EndToStart -> {
                onDelete(task)
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }

            dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd &&
                    dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd -> {
                onEdit(task)
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.White
                    SwipeToDismissBoxValue.StartToEnd -> Color.Yellow
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                },
                label = "backgroundColor"
            )

            val icon = when (dismissState.targetValue) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                else -> Icons.Default.Edit
            }

            val alignment = when (dismissState.targetValue) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.Center
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 8.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Swipe Action",
                    tint = Color.White
                )
            }
        },
        content = {
            Card(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .clickable {
                        onToggleComplete(task.copy(isCompleted = true))
                    },
                shape = RoundedCornerShape(6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = task.isCompleted,
                        onClick = {
                            val updatedTask = task.copy(isCompleted = !task.isCompleted)
                            onToggleComplete(updatedTask)
                        }
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                color = if (isOverdue) Color.Red else Color.Unspecified
                            )
                        )
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                color = if (isOverdue) Color.Red else Color.Unspecified
                            )
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun PrioritySection(
    title: String,
    tasks: List<Task>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onDelete: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onEdit: (Task) -> Unit
) {

    val backgroundColor = when (title) {
        "High Priority" -> Color.Red
        "Medium Priority" -> Color(0xFFFF9800)
        "Low Priority" -> Color(0xFF2196F3)
        else -> Color.Gray
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = OutFitFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column {
                tasks.forEach { task ->
                    SwipeToDismissTask(
                        task = task,
                        onDelete = onDelete,
                        onToggleComplete = onToggleComplete,
                        onEdit = onEdit,
                        paddingValues = PaddingValues(0.dp)
                    )
                }
            }
        }
    }
}
