package com.aldeanos.playmotion.ui.explorar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aldeanos.playmotion.R;
import com.aldeanos.playmotion.config.ServerConfig;
import com.aldeanos.playmotion.config.UserData;
import com.aldeanos.playmotion.databinding.FragmentExplorarBinding;
import com.aldeanos.playmotion.model.Cancion;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExplorarFragment extends Fragment {

    FragmentExplorarBinding binding;
    String token;

    public static ExplorarFragment newInstance(String param1, String param2) {
        ExplorarFragment fragment = new ExplorarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExplorarBinding.inflate(inflater, container, false);

        token = getArguments().getString("token");


        ArrayList<Cancion> listaCanciones = new ArrayList<>();



        binding.rvCanciones.setAdapter(new CancionesAdapter(listaCanciones, new CancionesViewHolder.CancionesListener() {
            @Override
            public void onClic(int position) {
                Toast.makeText(getContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        }));

        binding.rvCanciones.setLayoutManager(new LinearLayoutManager(getActivity()));



        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = ServerConfig.serverURL + "/playmotion/rest/canciones.php";
        Uri.Builder uri = new Uri.Builder();

        uri.encodedPath(url);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.build().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.println(Log.DEBUG, "MANGO", response);

                        try {
                            JSONArray datos = new JSONArray(response);


                            for (int i = 0; i < datos.length(); i++){
                                JSONObject dato = (JSONObject) datos.get(i);

                                listaCanciones.add(new Cancion(dato.getString("nombre"), dato.getString("artista"), dato.getString("album"), Integer.parseInt(dato.getString("id"))));

                            }

                            binding.rvCanciones.getAdapter().notifyDataSetChanged();
                            Log.e("","");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.println(Log.DEBUG, "MANGO", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", token );

                return headers;
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        binding.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaCanciones.clear();

                String url = ServerConfig.serverURL + "/playmotion/rest/canciones.php";
                Uri.Builder uri = new Uri.Builder();

                uri.appendQueryParameter("busqueda", binding.tvBusqueda.getText().toString());

                uri.encodedPath(url);


                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.build().toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.println(Log.DEBUG, "MANGO", response);

                                try {
                                    JSONArray datos = new JSONArray(response);


                                    for (int i = 0; i < datos.length(); i++){
                                        JSONObject dato = (JSONObject) datos.get(i);

                                        listaCanciones.add(new Cancion(dato.getString("nombre"), dato.getString("artista"), dato.getString("album"), Integer.parseInt(dato.getString("id"))));

                                    }

                                    binding.rvCanciones.getAdapter().notifyDataSetChanged();
                                    Log.e("","");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.DEBUG, "MANGO", error.toString());
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();

                        headers.put("Authorization", token );

                        return headers;
                    }

                };

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

        return binding.getRoot();
    }
}