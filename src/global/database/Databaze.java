/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package global.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Oldřich
 * @param <E>
 */
public class Databaze<E> implements IDatabaze<E> {

    private EntityManagerFactory emf;
    private EntityManager em;
    private String databaze;
    private Class typ;
    private final String NAZEV;
    private final char PISMENO;
    private static Databaze instance = null;

    public Databaze(String ktera, Class<E> ty) {
        databaze = "./data/" + ktera + ".odb";
        this.NAZEV = ty.getSimpleName();
        this.PISMENO = Character.toLowerCase(ty.getSimpleName().charAt(0));
        this.typ = ty;
        
        File file = new File("./data/" + ktera + ".odb");
        System.out.println(file.getAbsolutePath());
    }

    public synchronized static Databaze getInstance() {
        if (instance == null) {
            instance = new Databaze<>("database", Object.class);
            instance.start();
        }
        return instance;
    }

    public synchronized static boolean closeInstance() {
        if (instance == null) {
            return false;
        } else {
            instance.konec();
            instance = null;
            return true;
        }
    }

    @Override
    public void start() {
        emf = Persistence.createEntityManagerFactory(databaze);
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @Override
    public void startAktualizace() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    @Override
    public void konec() {
        em.close();
        emf.close();
    }

    @Override
    public void uloz(E objekt) {
        startAktualizace();
        em.persist(objekt);
        em.getTransaction().commit();
    }

    @Override
    public void smaz(E objekt) {
        startAktualizace();
        em.remove(objekt);
        em.getTransaction().commit();
    }

    @Override
    public void aktualizuj() {
        startAktualizace();
        em.getTransaction().commit();
    }

    @Override
    public E vratJeden(int id) {
        startAktualizace();
        Object s = em.find(typ, id);
        return (E) s;
    }

    @Override
    public List<E> vratVsechny() {
        startAktualizace();
        if (vratPocet() <= 0) {
            return new ArrayList<>();
        }
        TypedQuery<E> query
                = (TypedQuery<E>) em.createQuery("SELECT " + PISMENO + "  FROM " + NAZEV + " " + PISMENO, typ);
        List<E> results = query.getResultList();
        return results;
    }

    @Override
    public List<E> vratDlepodminky(String podminka) {
        startAktualizace();
        TypedQuery<E> query
                = (TypedQuery<E>) em.createQuery("SELECT " + PISMENO + "  FROM " + NAZEV + " " + PISMENO + " WHERE " + podminka, typ);

        List<E> results = query.getResultList();
        return results;
    }

    @Override
    public long vratPocet() {
        startAktualizace();
        long pocet;
        TypedQuery query
                = em.createQuery("SELECT COUNT(" + PISMENO + ") FROM " + NAZEV + " " + PISMENO, typ);
        pocet = (Long) query.getSingleResult();
        return pocet;
    }

    public double vratVysledekFce(String fce, String naCem) {  ///výčty? //mazání záznamů
        startAktualizace();
        return 0;
    }
}
