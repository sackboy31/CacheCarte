package mobe.m2dl.cachecarte;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Random;

public class PlateauCarteActivity extends Activity {

    private static final int NB_CARTES = 4;
    private boolean masquerCartes = false;
    private LinearLayout rang1, rang2;
    private int nombreCartesSelectionnees = 0; // Nombre de cartes sélectionnées dans toute la partie
    private int carteSelectionnee = 0; // Nombre de cartes sélectionnées pour le tour en cours.

    private LinkedList<Integer> listeNumero = new LinkedList<>();

    private LinkedList<Carte> cartes = new LinkedList<>();

    private boolean tourJoueur;     // Le joueur peut JOUER ou ATTENDS SON TOUR
    private boolean passerAdversaire = false;  // Récupère l'action PASSER de l'adversaire
    private int pointsAdversaire;

    private Button masquerButton, passerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plateau_carte);

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
        masquerButton.setEnabled(false);


        // Si IA, on récupère PASSER
        Bundle extras = getIntent().getExtras();

        if(extras != null && extras.getString("Passer")!= null){
            passerAdversaire = true;
            scoreIA();
            Toast toast2 = Toast.makeText(this, String.valueOf(pointsAdversaire), Toast.LENGTH_LONG);
            toast2.show();
        }

    }

    public void onClickPasserTour(View view){
        // Fin de tour, partie finie pour le joueur.
        masquerButton.setEnabled(false);


            // Si on a utilisé la connexion
            // TODO Tester si le joueur a aussi envoyer un PASSER


            if(passerAdversaire){
                // FIN DE PARTIE POUR LES DEUX JOUEURS
                // Calculer score du joueur et l'envoyer à l'autre joueur
                int pointsJoueur = calculCartesSelectionnee();
                // TODO Récupérer scoreSelection autre joueur
                //int pointsAdversaire = ...


                Intent intentNewJoinGame = new Intent(PlateauCarteActivity.this, FinDePartie.class);
                // Envoie à la vue suivante le score et le statut
                if(pointsJoueur > pointsAdversaire){
                    intentNewJoinGame.putExtra("score", String.valueOf(scoreJoueur()));
                    intentNewJoinGame.putExtra("statut", "GAGNANT");
                } else if (pointsJoueur == pointsAdversaire){
                    intentNewJoinGame.putExtra("score", String.valueOf(scoreJoueur()));
                    intentNewJoinGame.putExtra("statut", "EGALITE");
                } else {
                    intentNewJoinGame.putExtra("score", String.valueOf(0));
                    intentNewJoinGame.putExtra("statut", "PERDANT");
                }
                startActivity(intentNewJoinGame);
            }
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

            if(nombreCartesSelectionnees > 0 && carteSelectionnee < 2 && nombreCartesSelectionnees < cartes.size()){
                masquerButton.setEnabled(true);
            } else {
                masquerButton.setEnabled(false);
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
        masquerButton.setText("Montrer carte");
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
            masquerButton.setText("Cacher carte");
            masquerButton.setEnabled(false);
        }
    }

    private int calculCartesSelectionnee(){
        int sommeSelection = 0;
        for(int i = 0; i < cartes.size(); i++){
            Carte carteCourante = cartes.get(i);
            if(carteCourante.isSelection()){
                sommeSelection += carteCourante.getNumeroCarte();
            }
        }
        return sommeSelection;
    }

    private int scoreJoueur(){
        int score = 0;
        for(int i = 0; i < cartes.size(); i++){
            Carte carteCourante = cartes.get(i);
            if(!carteCourante.isSelection()){
                score += carteCourante.getNumeroCarte();
            }
        }
        return score;
    }

    private LinkedList<Integer> creationAléatoireCartesIAJoueur(){
        listeNumero.clear();
        for(int i = 0; i < NB_CARTES; i++){
            int numero;
            do {
                numero = new Random().nextInt(10-1 +1) + 1;
            } while (listeNumero.contains(numero));
            listeNumero.add(numero);
        }
        return listeNumero;
    }

    private void scoreIA(){
        for(int i = 0; i < NB_CARTES-1; i++){
            int numero;
            do {
                numero = new Random().nextInt(10-1 +1) + 1;
            } while (listeNumero.contains(numero));
            pointsAdversaire += numero;
        }
    }
}
