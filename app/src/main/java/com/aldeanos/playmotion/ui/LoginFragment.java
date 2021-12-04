package com.aldeanos.playmotion.ui;

import android.content.Context;
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

import com.aldeanos.playmotion.R;
import com.aldeanos.playmotion.config.ServerConfig;
import com.aldeanos.playmotion.config.UserData;
import com.aldeanos.playmotion.databinding.FragmentLoginBinding;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

        NavController navigation = NavHostFragment.findNavController(LoginFragment.this);

        UserData.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE)).getString(UserData.TOKEN_KEY);

        //TODO: Verificar el token, si es correcto navegar directamente a Explorar.
        //TODO: Ocultar los elementos de entrada y poner una pantalla de carga si no se ha conectado al servicio de tokens.

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (binding.etUsuario.getText().toString().equals("") && binding.etPass.getText().toString().equals("")) {
                    Toast.makeText(getContext(), getString(R.string.missing_information), Toast.LENGTH_SHORT).show();
                } else {

                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url = ServerConfig.serverURL + "/playmotion/rest/login.php";
                    Uri.Builder uri = new Uri.Builder();

                    uri.encodedPath(url);

                    uri.appendQueryParameter("usuario", binding.etUsuario.getText().toString());
                    uri.appendQueryParameter("contrasenia", binding.etPass.getText().toString());

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.build().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.println(Log.DEBUG, "MANGO", response);

                                    //TODO: loggearse solo cuando hay token
                                    try {
                                        JSONObject respuesta = new JSONObject(response);

                                        String login = respuesta.getString("login");

                                        if (login.equals("yes")) {

                                            String token = respuesta.getString("jwt");
                                            UserData.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE)).saveString(UserData.TOKEN_KEY, token);

                                            explorar(navigation);
                                        } else {
                                            Toast.makeText(getContext(), getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                                        }
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
                            return headers;
                        }

                    };

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }

            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), ServerConfig.serverURL, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void explorar(NavController navigation) {
        Bundle bundle = new Bundle();
        bundle.putString("token", UserData.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE)).getString(UserData.TOKEN_KEY));
        navigation.navigate(R.id.Navigate_Login_Explorar, bundle);
    }

}