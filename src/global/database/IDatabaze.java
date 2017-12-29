/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package global.database;

import java.util.List;

/**
 *
 * @author Oldřich
 */
public interface IDatabaze<E> {

    void aktualizuj();

    void konec();

    void smaz(E objekt);

    void start();

    void startAktualizace();

    void uloz(E objekt);

    List<E> vratDlepodminky(String podminka);

    E vratJeden(int id);

    long vratPocet();

    List<E> vratVsechny();
    
}
