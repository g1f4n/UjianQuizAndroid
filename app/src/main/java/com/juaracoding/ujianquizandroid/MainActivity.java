package com.juaracoding.ujianquizandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.juaracoding.ujianquizandroid.activity.HasilUjian;
import com.juaracoding.ujianquizandroid.model.QuizModel;
import com.juaracoding.ujianquizandroid.service.APIClient;
import com.juaracoding.ujianquizandroid.service.APIInterfacesRest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//  buat variable
    TextView txtQuestion;
    ImageView imgQuiz;
    Button btnA, btnB, btnC, btnD;
    RadioGroup radioGroup;

    int x;
    ArrayList<String> y;

    ArrayList<String> jawaban = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




//      findViewById
        txtQuestion = findViewById(R.id.txtQuestion);
        imgQuiz = findViewById(R.id.imgQuiz);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        radioGroup = findViewById(R.id.radioGroup);

        btnA.setId(Integer.parseInt("1"));
        btnB.setId(Integer.parseInt("2"));
        btnC.setId(Integer.parseInt("3"));
        btnD.setId(Integer.parseInt("4"));

        getQuestion();

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

//        btnA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getQuestion();
//            }
//        });




    }


//  get Question From Api Server
    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    public void getQuestion(){

        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<QuizModel> call3 = apiInterface.getQuestion();
        call3.enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {
                progressDialog.dismiss();
                final QuizModel data = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (data !=null) {

                    if (x < data.getData().getSoalQuizAndroid().size()) {
                        txtQuestion.setText(data.getData().getSoalQuizAndroid().get(x).getPertanyaan());
                        btnA.setText(data.getData().getSoalQuizAndroid().get(x).getA());
                        btnB.setText(data.getData().getSoalQuizAndroid().get(x).getB());
                        btnC.setText( data.getData().getSoalQuizAndroid().get(x).getC());
                        btnD.setText( data.getData().getSoalQuizAndroid().get(x).getD());


                        String image = data.getData().getSoalQuizAndroid().get(x).getGambar();
                        Picasso.get().load(image).into(imgQuiz);

//                        ArrayList<String> kunciJawaban = new ArrayList<>(data.getData().getSoalQuizAndroid().size());
//                        kunciJawaban.add("4");
//                        kunciJawaban.add("1");
//                        kunciJawaban.add("1");
//
//                        Toast.makeText(MainActivity.this, String.valueOf(x), Toast.LENGTH_LONG).show();
//
//                        if (jawaban.get(x).equalsIgnoreCase(kunciJawaban.get(x))) {
//                            y += Integer.parseInt(data.getData().getSoalQuizAndroid().get(x).getPoint());
//                        }



//                        if (data.getData().getSoalQuizAndroid().get(x).getJawaban() ==  ) {
//
//                        }

//                        Toast.makeText(MainActivity.this, String.valueOf(y), Toast.LENGTH_LONG).show();





                    }

                    x++;











                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });




    }




    @Override
    public void onClick(View v) {
        jawaban.add(String.valueOf(v.getId()));
        getQuestion();
        if (x == 3) {

            Intent i = new Intent(MainActivity.this, HasilUjian.class);
            i.putStringArrayListExtra("skor", jawaban);
            startActivity(i);



//                        data.getData().getSoalQuizAndroid().get(0).getJawaban() != data.getData().getSoalQuizAndroid().get(x).getA();
//                        Toast.makeText(this, "Salah bung", Toast.LENGTH_LONG).show();
        }

    }
}
