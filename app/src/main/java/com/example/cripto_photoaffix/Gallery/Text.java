package com.example.cripto_photoaffix.Gallery;

import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;

public class Text extends Media {
    public void accept(MediaVisitor visitor) {}

    @Override
    public File share(String sharingPath) {
        File file = new File(sharingPath, "sharing.png");


        return file;
    }

    @Override
    public void store(String path) {}
}
