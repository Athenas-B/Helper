/*
 * Copyright (C) 2017 olda9
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package images;

import global.SynchronizedObservableQueue;
import global.Settings;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author olda9
 */
public class ThreadManager extends Thread {

    private final Queue<Url> LINKS;
    private final Queue<Url> IMAGES;
    private final Map<Url, Url> CHECKED_URLS;
    private final ArrayList<FindImages> THREADS;
    private int maxThreads;
    private int maxLevel;
    private boolean stayOnDomain;

    ///TODO: STOP nondeprecated alternative
    ///TODO: actualy check if link was already checked
    ///TODO: try getting notification from threads, that they are finished, also somehow notifie the user, that all tasks are done
    
    public ThreadManager(Url startUrl) {
        this.LINKS = new SynchronizedObservableQueue();
        this.IMAGES = new SynchronizedObservableQueue();
        this.CHECKED_URLS = new ConcurrentHashMap();
        this.THREADS = new ArrayList<>();
        this.maxThreads = 2;
        this.maxLevel = 2;
        this.LINKS.add(startUrl);
    }

    @Override
    public void run() {
        if (!LINKS.isEmpty() && THREADS.size() < maxThreads) {
            FindImages t = new FindImages();
            THREADS.add(t);
            t.setImages(IMAGES);
            t.setLinks(LINKS);
            t.setMaxLevel(maxLevel);
            t.setCheckedUrls(CHECKED_URLS);
            t.setStayOnDomain(stayOnDomain);
            t.setDaemon(true);
            t.start();
            try {
                t.wait(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stopNow() {
        for (FindImages th : THREADS) {
            th.setPleaseDie(true);
         
        }

        this.interrupt();
    }

    public boolean isStayOnDomain() {
        return stayOnDomain;
    }

    public void setStayOnDomain(boolean stayOnDomain) {
        this.stayOnDomain = stayOnDomain;
    }

    public Queue<Url> getLINKS() {
        return LINKS;
    }

    public Queue<Url> getIMAGES() {
        return IMAGES;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Map<Url, Url> getCHECKED_URLS() {
        return CHECKED_URLS;
    }

    public static void main(String[] args) {
        ThreadManager tm = new ThreadManager(new Url(Settings.TEST_URL2, 0));
        tm.run();
        //System.out.println(tm.IMAGES);
        for (Url url : tm.IMAGES) {
            System.out.println(url.url);
        }
        //System.out.println(tm.LINKS);
    }
}
