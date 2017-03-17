package mobe.m2dl.cachecarte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickGame(View view) {
        Intent intentNewJoinGame = new Intent(this, NewOrJoinGame.class);
        startActivity(intentNewJoinGame);
    }
}
