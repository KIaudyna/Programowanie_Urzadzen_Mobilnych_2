package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication2.ui.theme.MyApplication2Theme
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import kotlinx.serialization.Serializable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.material3.RadioButton
import androidx.compose.foundation.layout.Row
import kotlin.time.measureTimedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.sp

//-----------------------------------------------------------------------Klasy
data class Film(
    val id: Int,
    val tytul: String,
    val rezyser: String,
    val rok: Int,
    val gatunek: String,
    val opis: String
)

data class Recenzja(
    val autor: String,
    val ocena: Double,
    val tresc: String
)

data class SzczegolyFilmu(
    val film: Film,
    val recenzje: List<Recenzja>,
    val podobneFilmy: List<Film>
)
//---------------------------------------------------------------------------Baza filmow
val listaFilmow = listOf(
    Film(1,"Pretty Woman", "Pretty Woman", 1990, "komedia romantyczna", "Film opowiada o znajomości bogatego biznesmena z Nowego Jorku, Edwarda Lewisa (Richard Gere), oraz żywiołowej dziewczyny do towarzystwa z Los Angeles, Vivian Ward (Julia Roberts)"),
    Film(2, "Diuna", "Denis Villeneuve", 2021, "Sci-Fi", "Młody książę trafia na pustynną planetę Arrakis."),
    Film(3,"Burleska", "Steve Antin", 2010, "komedia romantyczna", "Dzięki talentowi oraz samozaparciu Ali zdobywa pracę w rewii Burlesque Lounge, gdzie z dnia na dzień staje się gwiazdą lokalu."),
    Film(4, "Legalna blondynka", "Robert Luketic", 2002, "komedia", "Porzucona Elle postanawia odzyskać utraconą miłość, udowadniając byłemu chłopakowi, że jest równie błyskotliwa jak on. W tym celu rozpoczyna studia prawnicze."),
    Film(5, "Titanic", "James Cameron", 1997, "Romans", "Historia miłości na pokładzie słynnego statku."),
    Film(6, "Piraci z Karaibów: Klątwa Czarnej Perły", "Gore Verbinski", 2003, "przygodowy", "Kowal Will Turner sprzymierza się z kapitanem Jackiem Sparrowem, by odzyskać swoją miłość - porwaną córkę gubernatora." ),
    Film(7, "Dama", "Juan Carlos Fresnadillo", 2024, "przygodowy", "Małżeństwo młodej kobiety z czarującym księciem zmienia się w walkę o przetrwanie, gdy zostaje ona złożona w ofierze ziejącemu ogniem smokowi."),
    Film(8, "Coco", "Lee Unkrich", 2017, "Animacja", "Chłopiec odkrywa magiczny świat zmarłych."),
    Film(9, "Matrix", "Lana i Lilly Wachowski", 1999, "Sci-Fi", "Haker odkrywa prawdziwą naturę rzeczywistości."),
    Film(10, "Lalka", "Maciej Kawalski", 2026, "romans", "Bogaty warszawski kupiec zakochuje się w zubożałej arystokratce.")
)

//-----------------------------------------------------------------------------Recenzje
val listaRecenzji = mapOf(
    1 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    2 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    3 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    4 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    5 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    6 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    7 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    8 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    9 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    ),
    10 to listOf(
        Recenzja("Ania", 9.0, "Wspaniala lomedia romantyczna!"),
        Recenzja("Nikola", 5.0, "Nie podobalo mi sie.")
    )
)

//------------------------------------------------------------------------------Podobne filmy
val listaPodobnychFilmow = mapOf(
    1 to listOf(listaFilmow[2],listaFilmow[3],listaFilmow[6]),
    2 to listOf(listaFilmow[3],listaFilmow[7],listaFilmow[8]),
    3 to listOf(listaFilmow[0],listaFilmow[3],listaFilmow[6]),
    4 to listOf(listaFilmow[0],listaFilmow[2],listaFilmow[6]),
    5 to listOf(listaFilmow[0],listaFilmow[1],listaFilmow[8]),
    6 to listOf(listaFilmow[2],listaFilmow[9],listaFilmow[8]),
    7 to listOf(listaFilmow[0],listaFilmow[1],listaFilmow[2]),
    8 to listOf(listaFilmow[1],listaFilmow[5],listaFilmow[6]),
    9 to listOf(listaFilmow[0],listaFilmow[2],listaFilmow[3]),
    10 to listOf(listaFilmow[0],listaFilmow[2],listaFilmow[3])
)

//----------------------------------------------------------------------------Funkcje suspend
suspend fun fetchPopularMovies(): List<Film>{
    delay(2000)
    return listaFilmow
}

suspend fun fetchMovieDetails(movieId: Int): SzczegolyFilmu{
    delay(1500)

    val wybranyFilm = listaFilmow.find { it.id == movieId } !!

    return SzczegolyFilmu(
        film = wybranyFilm,
        recenzje = emptyList(),
        podobneFilmy = emptyList()
    )
}

suspend fun fetchMovieReviews(movieId: Int): List<Recenzja>{
    delay(3000)

    val recenzjaDoFilmu = listaRecenzji.getValue(movieId)

    return recenzjaDoFilmu
}

suspend fun fetchSimilarMovies(movieId: Int): List<Film>{
    delay(2000)

    val podobneFilmy = listaPodobnychFilmow.getValue(movieId)

    return podobneFilmy
}

suspend fun pobierzWszystkieSzczegoly(id: Int): SzczegolyFilmu {

    return coroutineScope {

        val dane = async { fetchMovieDetails(id) }
        val recenzje = async { fetchMovieReviews(id) }
        val podobne = async { fetchSimilarMovies(id) }

        SzczegolyFilmu(
            film = dane.await().film,
            recenzje = recenzje.await(),
            podobneFilmy = podobne.await()
        )
    }
}

suspend fun pobierzSekwencyjnie(id: Int): SzczegolyFilmu {

    val dane = fetchMovieDetails(id)
    val recenzje = fetchMovieReviews(id)
    val podobne = fetchSimilarMovies(id)

    return SzczegolyFilmu(
        film = dane.film,
        recenzje = recenzje,
        podobneFilmy = podobne
    )
}

//------------------------------------------------------------------Trasy
@Serializable
object ListaFilmowRoute

@Serializable
data class SzczegolyRoute(
    val id: Int
)



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplication2Theme {

                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFFFC0CB)
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = ListaFilmowRoute,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 20.dp)
                    ) {

                        // Ekran listy filmow
                        composable<ListaFilmowRoute> {

                            Greeting(
                                onFilmClick = { id ->

                                    navController.navigate(
                                        SzczegolyRoute(id)
                                    )

                                }
                            )
                        }

                        // Ekran szczegolow filmu
                        composable<SzczegolyRoute> { wpis ->

                            val trasa = wpis.toRoute<SzczegolyRoute>()

                            EkranSzczegolow(

                                id = trasa.id,
                                onWstecz = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    onFilmClick: (Int) -> Unit
) {

    var filmy by remember {
        mutableStateOf<List<Film>>(emptyList())
    }

    var czyLaduje by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        filmy = fetchPopularMovies()
        czyLaduje = false
    }


    if (czyLaduje) {

        CircularProgressIndicator()

    } else {
        Column {
            Text(
                text = "Kino GURU",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 20.dp)
            )

        LazyColumn {
            items(filmy) { film ->
                Text(
                    text = film.tytul,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onFilmClick(film.id)
                        }
                        .padding(vertical = 12.dp)
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.DarkGray
                )
            }
        }
    }
}}

@Composable
fun EkranSzczegolow(
    id: Int,
    onWstecz: () -> Unit
) {
    var szczegoly by remember {
        mutableStateOf<SzczegolyFilmu?>(null)
    }

    var czyLaduje by remember {
        mutableStateOf(true)
    }

    var tryb by remember {
        mutableStateOf("Współbieżnie")
    }

    var zastosowanyTryb by remember {
        mutableStateOf("")
    }

    var czasPobierania by remember {
        mutableStateOf("")
    }

    LaunchedEffect(id) {
        czyLaduje = true

        val (wynik, czas) = measureTimedValue {
            pobierzWszystkieSzczegoly(id)
        }

        szczegoly = wynik
        zastosowanyTryb = "Współbieżnie"
        czasPobierania = czas.toString()

        czyLaduje = false
    }

    val zakresKorutyn = rememberCoroutineScope()

    Column {

        Button(
            onClick = {
                onWstecz()
            }
        ) {
            Text("Wstecz")
        }


            Text("Tryb pobierania:", fontWeight = FontWeight.Bold)

            Row {
                RadioButton(
                    selected = tryb == "Współbieżnie",
                    onClick = {
                        tryb = "Współbieżnie"
                    }
                )
                Text("Współbieżnie")
            }

            Row {
                RadioButton(
                    selected = tryb == "Sekwencyjnie",
                    onClick = {
                        tryb = "Sekwencyjnie"
                    }
                )
                Text("Sekwencyjnie")
            }

        Button(
            onClick = {
                zakresKorutyn.launch {
                    czyLaduje = true

                    val wybranyTryb = tryb

                    val (wynik, czas) = measureTimedValue {
                        if (wybranyTryb == "Współbieżnie") {
                            pobierzWszystkieSzczegoly(id)
                        } else {
                            pobierzSekwencyjnie(id)
                        }
                    }

                    szczegoly = wynik
                    zastosowanyTryb = wybranyTryb
                    czasPobierania = czas.toString()

                    czyLaduje = false
                }
            },
            enabled = !czyLaduje
        ) {
            Text("Wczytaj ponownie")
        }

        if (czyLaduje) {

            CircularProgressIndicator()

        } else {

            szczegoly?.let { dane ->
                Spacer(modifier = Modifier.height(20.dp))
                Text("Zastosowany tryb: $zastosowanyTryb", fontWeight = FontWeight.Bold)
                Text("Czas pobierania: $czasPobierania", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))


                Text("Tytuł: ${dane.film.tytul}", fontWeight = FontWeight.Bold)
                Text("Reżyser: ${dane.film.rezyser}")
                Text("Rok: ${dane.film.rok}")
                Text("Gatunek: ${dane.film.gatunek}")
                Spacer(modifier = Modifier.height(20.dp))

                Text("Opis", fontWeight = FontWeight.Bold)
                Text(dane.film.opis)
                Spacer(modifier = Modifier.height(20.dp))

                Text("Recenzje", fontWeight = FontWeight.Bold)

                dane.recenzje.forEach { recenzja ->
                    Text("${recenzja.autor}: ${recenzja.ocena}/10")
                    Text(recenzja.tresc)
                }
                Spacer(modifier = Modifier.height(20.dp))

                Text("Podobne filmy", fontWeight = FontWeight.Bold)

                dane.podobneFilmy.forEach { film ->
                    Text(film.tytul)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplication2Theme {
        Greeting(onFilmClick = {})
    }
}