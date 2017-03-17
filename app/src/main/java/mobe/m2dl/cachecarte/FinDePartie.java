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
        if (extras != null) {
            int score = extras.getInt("score");
            TextView textViewScore = (TextView)findViewById(R.id.text_value_score);
            textViewScore.setText(score);
        }
    }

    public void onClickBackMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
