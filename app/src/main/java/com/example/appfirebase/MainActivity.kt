package com.example.appfirebase

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appfirebase.ui.theme.AppFirebaseTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppFirebaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(db)
                }
            }
        }
    }
}

@Composable
fun App(db: FirebaseFirestore) {
    var nome by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var listaClientes by remember { mutableStateOf(emptyList<Map<String, String>>()) }

    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {}

        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "João Lucas 3 DS 2024")
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {}

        // Adicionando campos de Nome e Telefone para os Clientes
        Row(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = "Nome:")
            }
            Column(
                Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = "Telefone:")
            }
        }

        // Input para Nome
        Row(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.fillMaxWidth(0.3f)
            ) {
                Text(text = "Nome:")
            }
            Column {
                TextField(
                    value = nome,
                    onValueChange = { nome = it }
                )
            }
        }

        // Input para Número
        Row(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.fillMaxWidth(0.3f)
            ) {
                Text(text = "Número:")
            }
            Column {
                TextField(
                    value = numero,
                    onValueChange = { numero = it }
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {}

        // Botão para cadastrar Cliente
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                val Cliente = hashMapOf(
                    "nome" to nome,
                    "numero" to numero
                )

                db.collection("Cliente").add(Cliente)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }
            }) {
                Text(text = "Cadastrar")
            }
        }

        // Seção para listar Clientes cadastrados
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {}

        Row(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.fillMaxWidth()
            ) {
                db.collection("Cliente")
                    .get()
                    .addOnSuccessListener { documents ->
                        val Clientes = mutableStateListOf<HashMap<String, String>>()
                        for (document in documents) {
                            val lista = hashMapOf(
                                "nome" to "${document.data.get("nome")}",
                                "numero" to "${document.data.get("numero")}"
                            )
                            Clientes.add(lista)
                            Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        }
                        listaClientes = Clientes
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents:", exception)
                    }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(listaClientes) { Cliente ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(0.5f)) {
                                Text(text = Cliente["nome"] ?: "--")
                            }
                            Column(modifier = Modifier.weight(0.5f)) {
                                Text(text = Cliente["numero"] ?: "--")
                            }
                        }
                    }
                }

                // Exibindo a lista de Clientes
                listaClientes.forEach { Cliente ->

                    Row(
                        Modifier.fillMaxWidth()
                    ) {

                        Column {
                            Text(text = Cliente["numero"] ?: "")
                        }
                        Column {
                            Text(text = Cliente["nome"] ?: "")
                        }

                    Row(
                        Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier.fillMaxWidth(0.3f)
                        ) {
                            Text(text = "Nome:")
                        }
                        Column(
                            Modifier.fillMaxWidth(0.3f)
                        ) {
                            Text(text = "Número:")
                        }

                    }

                    }
                }
            }
        }
    }
}
