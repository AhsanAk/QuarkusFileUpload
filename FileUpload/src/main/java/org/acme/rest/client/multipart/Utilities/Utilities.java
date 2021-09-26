package org.acme.rest.client.multipart.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    public String getRandomFileName() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
    }

    public String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
