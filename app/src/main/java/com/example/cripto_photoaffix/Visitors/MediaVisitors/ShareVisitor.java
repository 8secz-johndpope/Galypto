package com.example.cripto_photoaffix.Visitors.MediaVisitors;

import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.ImageShareCommand;
import com.example.cripto_photoaffix.Commands.VideoShareCommand;
import com.example.cripto_photoaffix.Gallery.Picture;
import com.example.cripto_photoaffix.Gallery.Video;

public class ShareVisitor implements MediaVisitor {
    private Command share;

    public void visit(Picture picture) {
        share = new ImageShareCommand();
        share.addMedia(picture);
        share.execute();
    }

    public void visit(Video video) {
        share = new VideoShareCommand();
        share.addMedia(video);
        share.execute();
    }
}
