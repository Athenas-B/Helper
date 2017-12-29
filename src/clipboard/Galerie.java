/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clipboard;

import java.net.URI;

/**
 *
 * @author Oldřich
 */
public class Galerie extends Zaznam {

    private String nazev;
    private URI adresa;

    public Galerie(URI adresa) {
        this.adresa = adresa;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public URI getAdresa() {
        return adresa;
    }

    public void setAdresa(URI adresa) {
        this.adresa = adresa;
    }

    @Override
    public String toString() {
        return "Galerie{" + "id=" + getId() + "nazev=" + nazev + ", adresa=" + adresa
                + ", stav=" + getStav() + ", datumPridani=" + getDatumPridani()
                + ", datumZmeny=" + getDatumZmeny() + '}';
    }

}
