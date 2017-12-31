package images;

import global.Settings;
import static global.Settings.USER_AGENT;
import global.SynchronizedObservableQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class FindImages extends Thread {

    private Queue<Url> links;
    private Queue<Url> images;
    private Map<Url, Url> checkedUrls;
    private int maxLevel;
    //sum is around 32400 ms for limit 80, 50500 for 100, 15400 for 55, 8200 for 40
    private final int WAIT_LIMIT;
    private LinkChecker checker;
    private boolean stayOnDomain;
    private volatile boolean pleaseDie;

    public FindImages() {
        this.WAIT_LIMIT = 80;
        this.maxLevel = 1;
        this.stayOnDomain = true;
        this.pleaseDie = false;
    }

    @Override
    public void run() {
        try {
            setName("Find images thread");
            find();
        } catch (InterruptedException ex) {
            Logger.getLogger(FindImages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FindImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void find() throws InterruptedException, MalformedURLException {
        while (!links.isEmpty() && !pleaseDie) {
            Url u = links.poll();
            if (u == null) {
                return;
            }
            this.checker = new LinkChecker(u);
            if (checker.isAlive()) {
                if (checker.isImage()) {
                    images.add(u);
                } else {
                    images.addAll(getImageLinks(u));
                    links.addAll(getLinks(u));
                }
            }
            checkedUrls.put(u, u);
        }
    }

    private List<Url> getImageLinks(Url webSiteURL) {
        try {
            List<Url> srcL = new ArrayList<>();
            //Connect to the website and get the html
            Document doc = Jsoup.connect(webSiteURL.url).userAgent(USER_AGENT).get();

            //Get all elements with img tag ,
            Elements img = doc.getElementsByTag("img");
            for (Element el : img) {
                //for each element get the srs url
                String src = el.absUrl("src");
                if (src.isEmpty()) {
                    continue;
                }
                String hostUrl = new URL(webSiteURL.url).getHost();
                String imgUrl = new URL(src).getHost();
                if ((stayOnDomain && hostUrl.equals(imgUrl)) || !stayOnDomain) {
                    srcL.add(new Url(src, webSiteURL.level + 1));
                }
            }
            return srcL;

        } catch (IOException ex) {
            System.err.println("There was an error");
            Logger.getLogger(FindImages.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    private List<Url> getLinks(Url webSiteURL) {
        try {
            List<Url> hrefL = new ArrayList<>();
            //Connect to the website and get the html
            Document doc = Jsoup.connect(webSiteURL.url).userAgent(USER_AGENT).get();
            //Get all elements with A tag ,
            Elements a = doc.getElementsByTag("a");
            for (Element el : a) {
                //for each element get the srs url
                String href = el.absUrl("href");
                if (href.isEmpty()) {
                    continue;
                }
                String hostUrl = new URL(webSiteURL.url).getHost();
                String hrefUrl;
                try {
                    hrefUrl = new URL(href).getHost();
                } catch (MalformedURLException ex) {
                    hrefUrl = "";   //sometimes the href is empty, so this prevent accesing it
                }
                if ((webSiteURL.level <= maxLevel) && ((stayOnDomain && hostUrl.equals(hrefUrl)) || !stayOnDomain)) {
                    Url u = new Url(href, webSiteURL.level + 1);
                    if (!checkedUrls.containsKey(u)) {
                        hrefL.add(u);
                    }
                }
            }
            return hrefL;

        } catch (IOException ex) {
            System.err.println("There was an error");
            Logger.getLogger(FindImages.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public boolean isPleaseDie() {
        return pleaseDie;
    }

    public void setPleaseDie(boolean pleaseDie) {
        this.pleaseDie = pleaseDie;
    }

    public Queue<Url> getLinks() {
        return links;
    }

    public void setLinks(Queue<Url> links) {
        this.links = links;
    }

    public Queue<Url> getImages() {
        return images;
    }

    public void setImages(Queue<Url> images) {
        this.images = images;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Map<Url, Url> getCheckedUrls() {
        return checkedUrls;
    }

    public void setCheckedUrls(Map<Url, Url> checkedUrls) {
        this.checkedUrls = checkedUrls;
    }

// private void getImages(String src) throws IOException {
//        String folderPath = "./";
//        String folder = null;
//
//        //Exctract the name of the image from the src attribute
//        int indexname = src.lastIndexOf("/");
//
//        if (indexname == src.length()) {
//            src = src.substring(1, indexname);
//        }
//
//        indexname = src.lastIndexOf("/");
//        String name = src.substring(indexname, src.length());
//
//        System.out.println(name);
//
//        //Open a URL Stream
//        URL url = new URL(src);
//        InputStream in = url.openStream();
//
//        OutputStream out = new BufferedOutputStream(new FileOutputStream(folderPath + name));
//
//        for (int b; (b = in.read()) != -1;) {
//            out.write(b);
//        }
//        out.close();
//        in.close();
//
//    }
    public boolean isStayOnDomain() {
        return stayOnDomain;
    }

    public void setStayOnDomain(boolean stayOnDomain) {
        this.stayOnDomain = stayOnDomain;
    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        FindImages f = new FindImages();
        f.links = new SynchronizedObservableQueue();
        f.images = new SynchronizedObservableQueue();
        f.checkedUrls = new ConcurrentHashMap<>();
        f.links.add(new Url(Settings.TEST_URL3, 0));
        f.maxLevel = 1;
        f.find();

    }
}
