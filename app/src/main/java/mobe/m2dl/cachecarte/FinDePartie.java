package mobe.m2dl.cachecarte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinDePartie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_de_partie);


        Bundle extras = getIntent().getExtras();

        if(extras != null) {

            String score = extras.getString("score");
            String resultat = extras.getString("statut");
            TextView textViewScore = (TextView)findViewById(R.id.text_value_score);
            TextView textViewStatut = (TextView) findViewById(R.id.text_result);
            textViewScore.setText(score);
            textViewStatut.setText(resultat);
        }


    }

    public void onClickBackMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
