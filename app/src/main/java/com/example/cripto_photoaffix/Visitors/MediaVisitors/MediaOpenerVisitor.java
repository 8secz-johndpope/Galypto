package com.example.cripto_photoaffix.Visitors.MediaVisitors;


import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.DataTransferer;
import com.example.cripto_photoaffix.Factories.IntentsFactory.ImageViewerIntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.VideoViewerIntentFactory;
import com.example.cripto_photoaffix.Gallery.Picture;
import com.example.cripto_photoaffix.Gallery.Video;

public class MediaOpenerVisitor implements MediaVisitor {
    private MyActivity activity;

    public MediaOpenerVisitor(MyActivity activity) {
        this.activity = activity;
    }

    @Override
    public void visit(Picture picture) {
        IntentFactory factory = new ImageViewerIntentFactory(activity);

        DataTransferer transferer = DataTransferer.getInstance();
        transferer.setData(picture);

        activity.startActivity(factory.create());
    }

    @Override
    public void visit(Video video) {
        IntentFactory factory = new VideoViewerIntentFactory(activity);

        DataTransferer transferer = DataTransferer.getInstance();
        transferer.setData(video);

        activity.startActivity(factory.create());
    }
}
