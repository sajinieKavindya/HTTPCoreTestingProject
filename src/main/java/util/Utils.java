package util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Utils {

    public static File getFile(String name) throws URISyntaxException {

        URL resource = Utils.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }
    }
}
