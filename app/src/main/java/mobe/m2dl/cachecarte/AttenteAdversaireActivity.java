package mobe.m2dl.cachecarte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AttenteAdversaireActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attente_adversaire);
    }


    public void onClickCancel(View view){
        finish();
    }
}