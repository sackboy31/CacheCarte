package mobe.m2dl.cachecarte;

import android.widget.ImageView;

/**
 * Created by Camille on 17/03/2017.
 */

public class Carte {

    private int id;
    private int numeroCarte;
    private ImageView imageView;
    private boolean selection;

    public Carte(ImageView image, int numeroCarte){
        this.numeroCarte = numeroCarte;
        this.id = image.getId();
        this.imageView = image;
        this.selection = false;
    }

    public int getId() {
        return id;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public int getNumeroCarte(){
        return numeroCarte;
    }
}
