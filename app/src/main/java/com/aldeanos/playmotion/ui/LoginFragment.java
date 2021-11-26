package com.aldeanos.playmotion.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.aldeanos.playmotion.config.ServerConfig;
import com.aldeanos.playmotion.databinding.FragmentLoginBinding;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navigation = NavHostFragment.findNavController(LoginFragment.this);


                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = ServerConfig.serverURL + "/playmotion/rest/login.php";
                Uri.Builder uri = new Uri.Builder();

                uri.encodedPath(url);

                uri.appendQueryParameter("usuario", binding.etUsuario.getText().toString());
                uri.appendQueryParameter("contrasenia",binding.etPass.getText().toString());

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.build().toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.println(Log.DEBUG, "MANGO", response);
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                                //TODO: loggearse solo cuando hay token


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
                        return headers;
                    }

                };

                // Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}