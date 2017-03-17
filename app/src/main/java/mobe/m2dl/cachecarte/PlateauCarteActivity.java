package mobe.m2dl.cachecarte;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.LinkedList;

public class PlateauCarteActivity extends Activity {

    private static final int NB_CARTES = 4;
    private boolean masquerCartes = false;
    private LinearLayout rang1, rang2;
    private int nombreCartesSelectionnees = 0; // Nombre de cartes sélectionnées dans toute la partie
    private int carteSelectionnee = 0; // Nombre de cartes sélectionnées pour le tour en cours.

    private LinkedList<Integer> listeNumero = new LinkedList<>();

    private LinkedList<Carte> cartes = new LinkedList<>();

    private boolean tourJoueur;     // Le joueur peut JOUER

    private Button masquerButton, passerButton;

    private BluetoothDevice device ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plateau_carte);

        device = Propriété.getDevice();
        rang1 = (LinearLayout) findViewById(R.id.rang1);
        rang2 = (LinearLayout) findViewById(R.id.rang2);
        masquerButton = (Button) findViewById(R.id.button_cartes);
        passerButton = (Button) findViewById(R.id.button_passer);

        listeNumero.add(4);
        listeNumero.add(2);
        listeNumero.add(10);
        listeNumero.add(8);

        // Ajouter les cartes aux Layout.
        for(int i = 0; i < listeNumero.size(); i++){
            int numeroCarte = listeNumero.get(i);

            ImageView image = new ImageView(this);
            int id = i;
            image.setId(id);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(40,0,40,0);// Espacement entre les cartes
            image.setLayoutParams(lp);

            int carteId = getResources().getIdentifier("carte"+numeroCarte,"drawable", getPackageName());
            image.setImageResource(carteId);

            Carte carte = new Carte(image, numeroCarte);
            cartes.add(carte);
            image.setOnClickListener(onClickCarte);

            if(i%2 == 0){
                rang1.addView(image);
            } else {
                rang2.addView(image);
            }
        }
    }

    public void onClickPasser(View view){
        // Fin de tour, partie finie pour le joueur.
        masquerButton.setEnabled(false);

        // TODO Tester si le joueur à aussi envoyer un PASSER


        //TODO Envoie passer en bluetooth
    }

    public void onClickMasquerAfficher(View view){

        // TODO Attendre début de tour connexion

        if(!masquerCartes){
            masquerCarte();
        } else {
            montrerCarte();
        }
    }

    private View.OnClickListener onClickCarte = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ImageView image = (ImageView) findViewById(v.getId());
            Carte carte = findCarte(v.getId());

            if(!carte.isSelection()){
                carte.setSelection(true);
                image.setPadding(8,8,8,8);
                image.setBackgroundColor(Color.RED);
                nombreCartesSelectionnees++;
                carteSelectionnee ++;
            } else {
                carte.setSelection(false);
                image.setPadding(0,0,0,0);
                image.setBackgroundColor(Color.TRANSPARENT);
                nombreCartesSelectionnees --;
                carteSelectionnee--;
            }

            if(nombreCartesSelectionnees > cartes.size() || carteSelectionnee > 1){
                masquerButton.setEnabled(false);
            } else {
                masquerButton.setEnabled(true);
            }

            if (carteSelectionnee > 0){
                passerButton.setEnabled(false);
            } else {
                passerButton.setEnabled(true);
            }
        }
    };

    private Carte findCarte(int id){
        for(int i = 0; i < cartes.size(); i ++){
            Carte carteCourante = cartes.get(i);
            if(carteCourante.getId() == id){
                return carteCourante;
            }
        }
        return null;
    }

    public LinkedList<Carte> getCartes(){
        return cartes;
    }

    /**
     * Méthode pour masquer les cartes
     */
    private void masquerCarte(){

        for(int i = 0; i < cartes.size(); i++){
            Carte carteCourante = cartes.get(i);
            ImageView image = carteCourante.getImageView();
            image.setImageResource(R.drawable.dos_carte);

            image.setOnClickListener(null);
            if(carteCourante.isSelection()){
                // Afficher la carte sélectionnée.
                image.setPadding(8,8,8,8);
                image.setBackgroundColor(Color.RED);
                // Désactiver la sélection sur les cartes sélectionnées

            }

        }
        masquerCartes = true;
        // TODO Send fin de tour
    }

    /**
     * Méthode pour afficher les cartes masquées
     */
    private void montrerCarte(){
        masquerCartes = false;

        for(int i = 0; i < cartes.size(); i++){
            Carte carteCourante = cartes.get(i);
            ImageView image = carteCourante.getImageView();

            int carteId = getResources().getIdentifier("carte"+carteCourante.getNumeroCarte(),"drawable", getPackageName());
            image.setImageResource(carteId);

            if(carteCourante.isSelection()){
                // Afficher la carte sélectionnée.
                image.setPadding(8,8,8,8);
                image.setBackgroundColor(Color.RED);
            } else {
                // Désactiver la sélection sur les cartes sélectionnées
                image.setOnClickListener(onClickCarte);
            }

            carteSelectionnee = 0;
            passerButton.setEnabled(true);
        }
    }
}
