package com.example.cripto_photoaffix.Visitors.MediaVisitors;

import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.ActivityTransferer;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.ImageViewerIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.VideoViewerIntentFactory;
import com.example.cripto_photoaffix.Gallery.Picture;
import com.example.cripto_photoaffix.Gallery.Video;

public class MediaOpenerVisitor implements MediaVisitor {
    @Override
    public void visit(Picture picture) {
        IntentFactory factory = new ImageViewerIntentFactory();

        DataTransferer transferer = DataTransferer.getInstance();
        transferer.setData(picture);

        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        activity.startActivity(factory.create());
    }

    @Override
    public void visit(Video video) {
        IntentFactory factory = new VideoViewerIntentFactory();

        DataTransferer transferer = DataTransferer.getInstance();
        transferer.setData(video);

        MyActivity activity = ActivityTransferer.getInstance().getActivity();
        activity.startActivity(factory.create());
    }
}
