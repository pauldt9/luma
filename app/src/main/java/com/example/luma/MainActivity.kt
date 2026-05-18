package com.example.luma

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.luma.ui.theme.LumaTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LumaTheme {
                AppNavigation()
            }
        }
    }
}

//@Composable
//fun AppNavigation() {
//
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "home"
//    ) {
//
//        composable("home") {
//            HomeScreen(navController)
//        }
//
//        composable("addTask") {
//            AddTaskScreen(navController)
//        }
//    }
//}
//
//@Composable
//fun HomeScreen(navController: NavController) {
//
//    val taskList = remember {
//        mutableStateListOf<Task>()
//    }
//
//    LaunchedEffect(Unit) {
//
//        val database = Firebase.database
//        val tasksRef = database.getReference("tareas")
//
//        tasksRef.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                taskList.clear()
//
//                for (taskSnapshot in snapshot.children) {
//
//                    val task = taskSnapshot.getValue(Task::class.java)
//
//                    if (task != null) {
//                        taskList.add(task)
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }
//
//    Scaffold(
//
//        floatingActionButton = {
//
//            FloatingActionButton(
//                onClick = {
//                    navController.navigate("addTask")
//                }
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Agregar"
//                )
//            }
//        }
//
//    ) { padding ->
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp),
//
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//
//            items(taskList) { task ->
//
//                Card(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//
//                        Text(
//                            text = task.title,
//                            style = MaterialTheme.typography.titleLarge
//                        )
//
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        Text(
//                            text = task.content,
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AddTaskScreen(navController: NavController) {
//
//    val context = LocalContext.current
//
//    var title by remember {
//        mutableStateOf("")
//    }
//
//    var content by remember {
//        mutableStateOf("")
//    }
//
//    Scaffold { padding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(20.dp),
//
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Text(
//                text = "Agregar tarea",
//                style = MaterialTheme.typography.headlineSmall
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            OutlinedTextField(
//                value = title,
//                onValueChange = {
//                    title = it
//                },
//                label = {
//                    Text("Título")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(
//                value = content,
//                onValueChange = {
//                    content = it
//                },
//                label = {
//                    Text("Contenido")
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(120.dp)
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Button(
//                onClick = {
//                    navController.popBackStack()
//                }
//            ) {
//                Text("Atrás")
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Button(
//                onClick = {
//
//                    if (title.isEmpty()) {
//                        Toast.makeText(
//                            context,
//                            "El título no puede estar vacío",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        return@Button
//                    }
//
//                    if (content.isEmpty()) {
//                        Toast.makeText(
//                            context,
//                            "El contenido no puede estar vacío",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        return@Button
//                    }
//
//                    addTaskToDatabase(
//                        title = title,
//                        content = content,
//                        context = context
//                    )
//
//                    navController.popBackStack()
//                }
//            ) {
//                Text("Guardar")
//            }
//        }
//    }
//}
//
//fun addTaskToDatabase(
//    title: String,
//    content: String,
//    context: android.content.Context
//) {
//
//    val database = Firebase.database
//
//    val tasksRef = database.getReference("tareas")
//
//    val key = tasksRef.push().key
//
//    val taskHashMap = hashMapOf(
//        "key" to key,
//        "title" to title,
//        "content" to content
//    )
//
//    if (key != null) {
//
//        tasksRef.child(key)
//            .setValue(taskHashMap)
//            .addOnCompleteListener {
//
//                Toast.makeText(
//                    context,
//                    "Tarea agregada",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            .addOnFailureListener {
//
//                Toast.makeText(
//                    context,
//                    "Error al guardar",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//    }
//}