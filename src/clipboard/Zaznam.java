/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clipboard;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

@Entity
/**
 *
 * @author Oldřich
 */
public class Zaznam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar datumPridani;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar datumZmeny;
    private EStav stav;

    public long getId() {
        return id;
    }

    public Calendar getDatumPridani() {
        return datumPridani;
    }

    public void setDatumPridani(Calendar datumPridani) {
        this.datumPridani = datumPridani;
    }

    public Calendar getDatumZmeny() {
        return datumZmeny;
    }

    public void setDatumZmeny(Calendar datumZmeny) {
        this.datumZmeny = datumZmeny;
    }

    public EStav getStav() {
        return stav;
    }

    public void setStav(EStav stav) {
        this.stav = stav;
    }

    @Override
    public String toString() {
        return "Zaznam{" + "id=" + id + ", datumPridani=" + datumPridani + ", datumZmeny=" + datumZmeny + ", stav=" + stav + '}';
    }

}
