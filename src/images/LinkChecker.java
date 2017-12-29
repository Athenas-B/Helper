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

import static global.Settings.HTTP_TIMEOUT;
import static global.Settings.USER_AGENT;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import static global.Settings.TEST_URL1;

/**
 *
 * @author olda9
 */
public class LinkChecker {

    private URL url;
    private int code;
    private String contentType;
    private int tried;

    public LinkChecker(Url url) throws MalformedURLException {
        this.url = new URL(url.url);
        this.code = HttpURLConnection.HTTP_BAD_METHOD;
        this.contentType = "";
        this.tried = 0;
    }

    private void tryIt() {
        HttpURLConnection connection = null;
        ++tried;
        try {
            connection = (HttpURLConnection) url.openConnection();
            //Set request to header to reduce load as Subirkumarsao said.       
            connection.setRequestMethod("HEAD");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setReadTimeout(HTTP_TIMEOUT);
            code = connection.getResponseCode();
            contentType = connection.getContentType();
            connection.disconnect();
        } catch (IOException ex) {
            System.err.println("↓↓ Cannot verify url, something went wrong here: " + url);
            //Logger.getLogger(Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isAlive() {
        if (tried < 1 || code != HttpURLConnection.HTTP_OK) {
            tryIt();
        }
        return code == HttpURLConnection.HTTP_OK;

    }

    public boolean isImage() {
        if (tried < 1 || code != HttpURLConnection.HTTP_OK) {
            tryIt();
        }
        return contentType.startsWith("image/");
    }

    public URL getUrl() {
        return url;
    }

    
}
