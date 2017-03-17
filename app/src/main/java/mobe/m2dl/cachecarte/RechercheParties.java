package mobe.m2dl.cachecarte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RechercheParties extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_parties);
    }

    public void onClickCancel(View view){
        finish();
    }

    public void onClickIA(View view){
        Intent intent = new Intent(RechercheParties.this, PlateauCarteActivity.class);
        intent.putExtra("Passer", "true");
        startActivity(intent);
    }
}
