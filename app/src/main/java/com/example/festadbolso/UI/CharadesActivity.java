package com.example.festadbolso.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.festadbolso.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CharadesActivity extends Activity {

    private TextView wordTextView;
    private TextView counterTextView;
    private Button skipButton, correctButton;
    private Button categoryAnimals, categoryMovies, categorySuperheroes, categoryActions, categoryNone;
    private Map<String, List<String>> wordsByCategory;
    private String currentCategory = "None"; // Default category
    private String currentWord;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charades);

        // Initialize views
        wordTextView = findViewById(R.id.wordTextView);
        counterTextView = findViewById(R.id.counterTextView);
        skipButton = findViewById(R.id.skipButton);
        correctButton = findViewById(R.id.correctButton);
        categoryAnimals = findViewById(R.id.categoryAnimals);
        categoryMovies = findViewById(R.id.categoryMovies);
        categorySuperheroes = findViewById(R.id.categorySuperheroes);
        categoryActions = findViewById(R.id.categoryActions);
        categoryNone = findViewById(R.id.categoryNone);

        // Populate the word list with categories
        populateWordList();

        // Set a random word initially
        currentWord = getRandomWord(currentCategory);
        wordTextView.setText(currentWord);

        // Set up button listeners
        skipButton.setOnClickListener(v -> {
            currentWord = getRandomWord(currentCategory);
            wordTextView.setText(currentWord);
        });

        correctButton.setOnClickListener(v -> {
            // Increment the score and update the counter TextView
            score++;
            counterTextView.setText("Pontuação: " + score);

            Toast.makeText(CharadesActivity.this, "Bom trabalho!", Toast.LENGTH_SHORT).show();
            currentWord = getRandomWord(currentCategory);
            wordTextView.setText(currentWord);
        });

        // Set up category button listeners
        categoryAnimals.setOnClickListener(v -> setCategory("Animals"));
        categoryMovies.setOnClickListener(v -> setCategory("Movies"));
        categorySuperheroes.setOnClickListener(v -> setCategory("Superheroes"));
        categoryActions.setOnClickListener(v -> setCategory("Actions"));
        categoryNone.setOnClickListener(v -> setCategory("None"));
    }

    private void populateWordList() {
        wordsByCategory = new HashMap<>();

// Add words for each category
        List<String> animals = new ArrayList<>();
        animals.add("Leão"); // Lion
        animals.add("Elefante"); // Elephant
        animals.add("Girafa"); // Giraffe
        animals.add("Canguru"); // Kangaroo
        animals.add("Pinguim"); // Penguin
        animals.add("Tigre"); // Tiger
        animals.add("Urso"); // Bear
        animals.add("Zebra"); // Zebra
        animals.add("Macaco"); // Monkey
        animals.add("Panda"); // Panda
        animals.add("Lobo"); // Wolf
        animals.add("Raposa"); // Fox
        animals.add("Veado"); // Deer
        animals.add("Coelho"); // Rabbit
        animals.add("Cavalo"); // Horse
        animals.add("Vaca"); // Cow
        animals.add("Ovelha"); // Sheep
        animals.add("Cabra"); // Goat
        animals.add("Porco"); // Pig
        animals.add("Galinha"); // Chicken
        animals.add("Crocodilo"); // Crocodile
        animals.add("Tubarão"); // Shark
        animals.add("Borboleta"); // Butterfly
        animals.add("Aranha"); // Spider
        animals.add("Coruja"); // Owl
        wordsByCategory.put("Animals", animals);

        List<String> movies = new ArrayList<>();
        movies.add("Titanic"); // Titanic
        movies.add("Avatar"); // Avatar
        movies.add("Inception"); // Inception
        movies.add("Jurassic Park"); // Jurassic Park
        movies.add("Matrix"); // The Matrix
        movies.add("Star Wars"); // Star Wars
        movies.add("O Padrinho"); // The Godfather
        movies.add("Forrest Gump"); // Forrest Gump
        movies.add("O Cavaleiro das Trevas"); // The Dark Knight
        movies.add("Pulp Fiction"); // Pulp Fiction
        movies.add("Fight Club"); // Fight Club
        movies.add("The Usual Suspects"); // The Usual Suspects
        movies.add("O Senhor dos Anéis"); // The Lord of the Rings
        movies.add("Harry Potter"); // Harry Potter
        movies.add("The Avengers"); // The Avengers
        movies.add("Interstellar"); // Interstellar
        movies.add("Gladiador"); // Gladiator
        movies.add("O Rei Leão"); // The Lion King
        movies.add("Toy Story"); // Toy Story
        movies.add("Finding Nemo"); // Finding Nemo
        movies.add("Cidade de Deus"); // City of God
        movies.add("Amélie"); // Amélie
        movies.add("The Great Dictator"); // The Great Dictator
        movies.add("O Fabuloso Destino de Amélie Poulain"); // Amélie
        movies.add("A Vida é Bela"); // Life is Beautiful
        wordsByCategory.put("Movies", movies);

        List<String> superheroes = new ArrayList<>();
        superheroes.add("Homem-Aranha"); // Spiderman
        superheroes.add("Batman"); // Batman
        superheroes.add("Super-Homem"); // Superman
        superheroes.add("Mulher-Maravilha"); // Wonder Woman
        superheroes.add("Homem de Ferro"); // Iron Man
        superheroes.add("Capitão América"); // Captain America
        superheroes.add("Thor"); // Thor
        superheroes.add("Hulk"); // Hulk
        superheroes.add("Pantera Negra"); // Black Panther
        superheroes.add("Flash"); // Flash
        superheroes.add("Aquaman"); // Aquaman
        superheroes.add("Green Lantern"); // Green Lantern
        superheroes.add("Wolverine"); // Wolverine
        superheroes.add("Deadpool"); // Deadpool
        superheroes.add("Viúva Negra"); // Black Widow
        superheroes.add("Doctor Strange"); // Doctor Strange
        superheroes.add("Ant-Man"); // Ant-Man
        superheroes.add("Captain Marvel"); // Captain Marvel
        superheroes.add("Hawkeye"); // Hawkeye
        superheroes.add("Vision"); // Vision
        superheroes.add("Storm"); // Storm
        superheroes.add("Cyclops"); // Cyclops
        superheroes.add("Jean Grey"); // Jean Grey
        superheroes.add("Magneto"); // Magneto
        superheroes.add("Nightcrawler"); // Nightcrawler
        wordsByCategory.put("Superheroes", superheroes);

        List<String> actions = new ArrayList<>();
        actions.add("Correr"); // Running
        actions.add("Saltar"); // Jumping
        actions.add("Nadar"); // Swimming
        actions.add("Dançar"); // Dancing
        actions.add("Cantar"); // Singing
        actions.add("Rir"); // Laughing
        actions.add("Chorar"); // Crying
        actions.add("Comer"); // Eating
        actions.add("Dormir"); // Sleeping
        actions.add("Ler"); // Reading
        actions.add("Escrever"); // Writing
        actions.add("Conduzir"); // Driving
        actions.add("Escalar"); // Climbing
        actions.add("Cair"); // Falling
        actions.add("Lutar"); // Fighting
        actions.add("Pontapear"); // Kicking
        actions.add("Socar"); // Punching
        actions.add("Atirar"); // Throwing
        actions.add("Apanhar"); // Catching
        actions.add("Assobiar"); // Whistling
        actions.add("Pular Corda"); // Jump Rope
        actions.add("Andar de Bicicleta"); // Riding a Bike
        actions.add("Fazer Yoga"); // Doing Yoga
        actions.add("Meditar"); // Meditating
        actions.add("Pintar"); // Painting
        wordsByCategory.put("Actions", actions);

        // Add all words to the "None" category
        List<String> allWords = new ArrayList<>();
        allWords.addAll(animals);
        allWords.addAll(movies);
        allWords.addAll(superheroes);
        allWords.addAll(actions);
        wordsByCategory.put("None", allWords);
    }

    private String getRandomWord(String category) {
        List<String> words = wordsByCategory.get(category);
        if (words == null || words.isEmpty()) {
            return "No words available";
        }
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    private void setCategory(String category) {
        currentCategory = category;
        currentWord = getRandomWord(currentCategory);
        wordTextView.setText(currentWord);
        Toast.makeText(this, "Category: " + category, Toast.LENGTH_SHORT).show();
    }
}