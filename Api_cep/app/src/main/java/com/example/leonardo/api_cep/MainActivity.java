package com.example.leonardo.api_cep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    private static MainActivity sInstance = null; //salvar a instancia pois nao é possivel usa-la em inner

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etCep = (EditText) findViewById(R.id.etCep);
        final TextView tvCid = (TextView) findViewById(R.id.tvCid);
        final TextView tvKm = (TextView) findViewById(R.id.tvArea);
        final TextView tvState = (TextView) findViewById(R.id.tvState);
        final TextView tvIbge = (TextView) findViewById(R.id.tvIbge);
        Button btnBusca = (Button) findViewById(R.id.btnBusca);

        sInstance = this;

        btnBusca.setOnClickListener(new View.OnClickListener() { //click do botão
            @Override
            public void onClick(View view) {

                tvCid.setText("");
                tvKm.setText("");
                tvState.setText("");
                tvIbge.setText("");

                String cep = etCep.getText().toString(); //validação
                if (cep == null || cep.length() != 8){
                    etCep.setError(getString(R.string.erro1));
                } else {
                    String url = "http://api.postmon.com.br/v1/cep/"+cep; //link da api com a string da Edit Text

                    JsonObjectRequest reqJson = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try { //consultas

                                        tvCid.setText(response.getString("cidade"));
                                        tvKm.setText(response.getJSONObject("cidade_info").getString("area_km2").concat(" Km²"));
                                        tvState.setText(response.getJSONObject("estado_info").getString("nome"));
                                        tvIbge.setText(response.getJSONObject("cidade_info").getString("codigo_ibge"));


                                    } catch (JSONException e) {

                                        Toast.makeText(getApplicationContext(), getString(R.string.erro2), Toast.LENGTH_LONG);
                                    }
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.erro2), Toast.LENGTH_LONG);
                                }
                            });
                    MySingleton.getInstance(sInstance).addToRequestQueue(reqJson);
                }
            }
        });


    }
}