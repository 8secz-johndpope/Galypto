package com.example.cripto_photoaffix.Visitors.MediaVisitors;

import com.example.cripto_photoaffix.Gallery.Picture;
import com.example.cripto_photoaffix.Gallery.Video;

public interface MediaVisitor {
    public void visit(Picture picture);
    public void visit(Video video);
}
