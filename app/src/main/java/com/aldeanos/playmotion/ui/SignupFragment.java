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

import com.aldeanos.playmotion.R;
import com.aldeanos.playmotion.config.ServerConfig;
import com.aldeanos.playmotion.databinding.FragmentSignupBinding;
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

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (binding.etName.getText().toString().equals("")) {
                    Toast.makeText(getContext(), getString(R.string.missing_name), Toast.LENGTH_SHORT).show();
                } else if (binding.etPass.getText().toString().equals("")) {
                    Toast.makeText(getContext(), getString(R.string.missing_pass), Toast.LENGTH_SHORT).show();
                } else if (binding.etDate.getText().toString().equals("")) {
                    Toast.makeText(getContext(), getString(R.string.missing_birthdate), Toast.LENGTH_SHORT).show();
                } else if (binding.etUserName.getText().toString().equals("") ) {
                    Toast.makeText(getContext(), getString(R.string.missing_user), Toast.LENGTH_SHORT).show();
                } else{

                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url = ServerConfig.serverURL + "/playmotion/rest/login.php";
                    Uri.Builder uri = new Uri.Builder();

                    uri.encodedPath(url);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uri.build().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.println(Log.DEBUG, "MANGO", response);


                                    //TODO: loggearse solo cuando hay token
                                    try {
                                        JSONObject respuesta = new JSONObject(response);

                                        String signup = respuesta.getString("signup");

                                        if (signup.equals("si")) {

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
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("nombre", binding.etName.getText().toString());
                            params.put("usuario", binding.etUserName.getText().toString());
                            params.put("contrasenia", binding.etPass.getText().toString());
                            params.put("fechanacimiento", binding.etDate.getText().toString());


                            return params;
                        }

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}