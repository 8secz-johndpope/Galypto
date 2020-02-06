package com.example.cripto_photoaffix.Visitors.MediaVisitors;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.ImageViewerIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.VideoViewerIntentFactory;
import com.example.cripto_photoaffix.Gallery.Picture;
import com.example.cripto_photoaffix.Gallery.Video;
import com.example.cripto_photoaffix.MediaTransferer;

public class MediaOpenerVisitor implements MediaVisitor {
    @Override
    public void visit(Picture picture) {
        IntentFactory factory = new ImageViewerIntentFactory();

      //  GalleryTransferer transferer = GalleryTransferer.getInstance();
       // transferer.setData(picture);
        MediaTransferer transferer = MediaTransferer.getInstance();
        transferer.setMedia(picture);

        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        activity.startActivity(factory.create());
    }

    @Override
    public void visit(Video video) {
        IntentFactory factory = new VideoViewerIntentFactory();

        //  GalleryTransferer transferer = GalleryTransferer.getInstance();
        // transferer.setData(video);
        MediaTransferer transferer = MediaTransferer.getInstance();
        transferer.setMedia(video);

        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        activity.startActivity(factory.create());
    }
}
