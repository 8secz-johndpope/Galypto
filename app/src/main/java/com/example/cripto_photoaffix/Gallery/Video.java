package com.example.cripto_photoaffix.Gallery;


public class Video extends Media {
    protected String path;
    public Video(String path) {
        this.path = path;
    }

    @Override
    public void open() {}

    private void findPreview() {}
}
