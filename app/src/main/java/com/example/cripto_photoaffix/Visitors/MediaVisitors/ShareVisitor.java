package com.example.cripto_photoaffix.Visitors.MediaVisitors;

import com.example.cripto_photoaffix.Commands.MediaCommands.MediaCommand;
import com.example.cripto_photoaffix.Commands.MediaCommands.ImageShareMediaCommand;
import com.example.cripto_photoaffix.Commands.MediaCommands.VideoShareMediaCommand;
import com.example.cripto_photoaffix.Gallery.Picture;
import com.example.cripto_photoaffix.Gallery.Video;

public class ShareVisitor implements MediaVisitor {
    private MediaCommand share;

    public void visit(Picture picture) {
        share = new ImageShareMediaCommand();
        share.execute(picture);
    }

    public void visit(Video video) {
        share = new VideoShareMediaCommand();
        share.execute(video);
    }
}
