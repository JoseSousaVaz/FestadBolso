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
                    .put("id", new JSONObject().put("integerValue", 3))
                    .put("name", new JSONObject().put("stringValue", "De quem é esta história?"))
                    .put("description", new JSONObject().put("stringValue", "Descubra mais sobre as pessoas à sua volta partilhando uma história que os vai apanhar de surpresa"))
                    .put("rules", new JSONObject().put("stringValue", "Preparação: Se forem mais do que 8 jogadores dividam-se em grupos de 5.\nRegras:\n1.Escolham uma pessoa para ser o leitor.\n2.Escrevam a vossa história mais engraçada ou mais estranha num papel ou no telemóvel.\n3.O leitor designado lê as histórias aleatoriamente e este(caso não saiba a quem pertence) e o resto do grupo tenta adivinhar quem escreveu essa história.\n4.Se o jogo tiver vários grupos permitam que membros do vosso grupo partilhem a vossa história com membros dos outros grupos."))
                    .put("maxPlayers", new JSONObject().put("integerValue", 20))
                    .put("difficulty", new JSONObject().put("integerValue", 1))
                    .put("setupTime", new JSONObject().put("integerValue", 1)) // minutes
                    .put("playTime", new JSONObject().put("integerValue", 25))  // minutes
                    .put("imageUrl", new JSONObject().put("stringValue", "https://www.icebreakerspot.com/cdn-cgi/imagedelivery/wqvb7H6GNzM_j_RtyG61hw/98788cf7-a1c2-4978-6fe7-e53358fe0900/w=1536,format=webp,quality=75"))
                    .put("createdAt", new JSONObject().put("timestampValue", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date())))
                    .put("featured", new JSONObject().put("booleanValue", false));

            // Add categories as array value
            JSONArray categoriesArray = new JSONArray();
            categoriesArray.put(new JSONObject().put("stringValue", "Offline"));
            categoriesArray.put(new JSONObject().put("stringValue", "Social"));
            categoriesArray.put(new JSONObject().put("stringValue", "Quebra gelo"));

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
}
