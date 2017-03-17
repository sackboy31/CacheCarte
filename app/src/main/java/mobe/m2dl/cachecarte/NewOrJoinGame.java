package mobe.m2dl.cachecarte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NewOrJoinGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_or_join_game);
    }

    public void onClickNewGame(View view) {
        Intent intentNewGame = new Intent(this, AttenteAdversaireActivity.class);
        startActivity(intentNewGame);
    }

    public void onClickJoinGame(View view) {
        Intent intentJoinGame = new Intent(this, RechercheParties.class);
        startActivity(intentJoinGame);
    }
}
