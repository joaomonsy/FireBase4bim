package com.example.appfirebase

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    val clientes = remember { mutableStateListOf<HashMap<String, String>>() }

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
            // Image with rounded borders and smaller size
            Image(
                painter = painterResource(id = R.drawable.palmeiras),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .size(100.dp)  // Adjust the size of the image
            )
        }

        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "João Lucas Monsynhatti Lima")
                Text(text = "3 DS Manhã")
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {}

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
                Text(text = "Telefone:")
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
                val cliente = hashMapOf(
                    "nome" to nome,
                    "numero" to numero
                )

                db.collection("Cliente").add(cliente)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                        Log.d(ContentValues.TAG, "Ultimo Cliente Cadastrado : $cliente")

                        // Recupera e exibe clientes cadastrados
                        db.collection("Cliente").get()
                            .addOnSuccessListener { result ->
                                clientes.clear()
                                for (document in result) {
                                    val lista = hashMapOf(
                                        "nome" to (document.getString("nome") ?: "--"),
                                        "numero" to (document.getString("numero") ?: "--")
                                    )
                                    clientes.add(lista)
                                    Log.i(ContentValues.TAG, "Lista: $lista")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error getting documents.", e)
                            }
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
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(0.5f)) {
                                Text(text = "Nome:")
                            }
                            Column(modifier = Modifier.weight(0.5f)) {
                                Text(text = "Telefone:")
                            }
                        }
                    }
                    items(clientes) { cliente ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(0.5f)) {
                                Text(text = cliente["nome"] ?: "--")
                            }
                            Column(modifier = Modifier.weight(0.5f)) {
                                Text(text = cliente["numero"] ?: "--")
                            }
                        }
                    }
                }
            }
        }
    }
}
