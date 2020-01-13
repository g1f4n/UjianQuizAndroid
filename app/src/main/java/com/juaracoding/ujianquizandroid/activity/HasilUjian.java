package com.juaracoding.ujianquizandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.juaracoding.ujianquizandroid.MainActivity;
import com.juaracoding.ujianquizandroid.R;
import com.juaracoding.ujianquizandroid.model.QuizModel;
import com.juaracoding.ujianquizandroid.service.APIClient;
import com.juaracoding.ujianquizandroid.service.APIInterfacesRest;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HasilUjian extends AppCompatActivity {

    TextView txtSkor;
    Button btnSelesai, btnUlang;

    ArrayList<String> stock;

    String hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_ujian);

        txtSkor = findViewById(R.id.txtSkor);
        btnSelesai = findViewById(R.id.btnSelesai);
        btnUlang = findViewById(R.id.btnUlang);


        Intent arr = getIntent();
        stock = arr.getStringArrayListExtra("skor");

        callQuiz();

        btnUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HasilUjian.this, MainActivity.class);
                startActivity(i);
//                finish();
            }
        });

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;

    public void callQuiz() {

        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(HasilUjian.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<QuizModel> call3 = apiInterface.getQuestion();
        call3.enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {
                progressDialog.dismiss();
                QuizModel data = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (data != null) {


                    Integer poin = 0;
                    Integer nilai = 0;

                    for (int x = 0; x < 3; x++) {

                        ArrayList<String> hope = new ArrayList<String>(data.getData().getSoalQuizAndroid().size());
                        hope.add("4");
                        hope.add("1");
                        hope.add("1");

                        if (stock.get(x).equalsIgnoreCase(hope.get(x))) {
                            poin = Integer.parseInt(data.getData().getSoalQuizAndroid().get(x).getPoint());
                            nilai += poin;
                        }


                        txtSkor.setText(String.valueOf(nilai));

                    }

                } else {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(HasilUjian.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(HasilUjian.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });


    }
}
