package com.example.festadbolso.Firestore;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;  // If you use Collections.emptyList() for an empty list


public class FirestoreHelper {
    private static final String TAG = "FirestoreHelper";
    private static final String FIRESTORE_URL = "https://firestore.googleapis.com/v1/projects/festadbolso/databases/(default)/documents/games";

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static void getGames(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, FIRESTORE_URL,
                response -> Log.d(TAG, "Games games: " + response),
                error -> Log.e(TAG, "Error retrieving games", error)
        );
        queue.add(request);
    }

    public static void addGames(Context context) {
        //Let's use this to test out new games to database
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://firestore.googleapis.com/v1/projects/festadbolso/databases/(default)/documents/games?key=AIzaSyC3Di6JbWETJbCMljmlg2ze8docxfW9fVc\n";

        JSONObject gameData = new JSONObject();
        try {
            // Create the fields object with all game properties
            JSONObject fields = new JSONObject()
                    .put("id", new JSONObject().put("integerValue", 7))
                    .put("name", new JSONObject().put("stringValue", "Espião"))
                    .put("description", new JSONObject().put("stringValue", "Descubra quem é o infiltrado no grupo."))
                    .put("rules", new JSONObject().put("stringValue", "Preparação: Os jogadores devem dispor-se de maneira confortável num circulo.\nRegras:\n1.A aplicação irá aleatoriamente escolher um(ou mais) espião e designar o resto dos jogadores como agentes.\n2.Na aplicação o espião vai receber uma informação a dizer que é o espião, o resto dos jogadores vão receber uma localização: praia,banco,escola,etc.\n3.Um jogador começa por perguntar a outro à sua escolha uma pergunta simples sobre a localização -Este sítio é dentro ou fora de um edificio?, analisa a resposta e decide se quer acusar o jogador que respondeu de ser o espião ou passar a vez.\n4.Se escolher acusar e estiver correcto o espião é expulso, se for o único ganharam o jogo. Se não estiver correcto quem acusou perde e sai do jogo.\n5.Não acusando a vez passa para o próximo, repetindo assim até apenas haver o espião e um agente(ou número igual de espiões e agentes."))
                    .put("maxPlayers", new JSONObject().put("integerValue", 8))
                    .put("difficulty", new JSONObject().put("integerValue", 2))
                    .put("setupTime", new JSONObject().put("integerValue", 1)) // minutes
                    .put("playTime", new JSONObject().put("integerValue", 10))  // minutes
                    .put("imageUrl", new JSONObject().put("stringValue", "https://static.vecteezy.com/system/resources/previews/008/222/212/non_2x/detective-spy-logo-free-vector.jpg"))
                    .put("createdAt", new JSONObject().put("timestampValue", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date())))
                    .put("featured", new JSONObject().put("booleanValue", true));

            // Add categories as array value
            JSONArray categoriesArray = new JSONArray();
            categoriesArray.put(new JSONObject().put("stringValue", "Online"));
            categoriesArray.put(new JSONObject().put("stringValue", "Social"));
            categoriesArray.put(new JSONObject().put("stringValue", "Social Deduction"));

            JSONObject arrayValues = new JSONObject();
            arrayValues.put("arrayValue", new JSONObject().put("values", categoriesArray));
            fields.put("categories", arrayValues);

            // Put fields into the main request object
            gameData.put("fields", fields);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, gameData,
                response -> Log.d("Firestore", "Game added successfully: " + response.toString()),
                error -> Log.e("Firestore", "Error adding game: " + error.toString())
        );

        queue.add(request);

    }

    // Function to retrieve the highest ID from the games collection
    public static void getHighestGameId(OnHighestIdRetrievedListener listener) {
        db.collection("games")
                .orderBy("id", Query.Direction.DESCENDING) // Order by the 'id' field in descending order
                .limit(1) // Get only the document with the highest 'id'
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        long highestId = documentSnapshot.getLong("id");
                        listener.onHighestIdRetrieved(highestId); // Callback with the highest ID
                    } else {
                        listener.onHighestIdRetrieved(0); // If no games exist, return 0 as the highest ID
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onError(e); // Handle error
                });
    }

    // Callback listener interface for getting the highest ID
    public interface OnHighestIdRetrievedListener {
        void onHighestIdRetrieved(long highestId);
        void onError(Exception e);
    }
    //Random game implementation
    public static void getNonFeaturedGameIds(OnIdsRetrievedListener listener) {
        db.collection("games")
                .whereEqualTo("featured", false)  // Filter to get only non-featured games
                .get()  // Perform the query
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Long> gameIds = new ArrayList<>();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Long gameId = documentSnapshot.getLong("id");  // Assuming the 'id' field is a Long
                            if (gameId != null) {
                                gameIds.add(gameId);  // Add the game ID to the list
                            }
                        }
                        listener.onIdsRetrieved(gameIds);  // Callback with the list of IDs
                    } else {
                        listener.onIdsRetrieved(Collections.emptyList());  // If no non-featured games found
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onError(e);  // Handle error
                });
    }
    public interface OnIdsRetrievedListener {
        void onIdsRetrieved(List<Long> gameIds);  // Callback for the retrieved list of game IDs
        void onError(Exception e);  // Callback for error handling
    }


}
