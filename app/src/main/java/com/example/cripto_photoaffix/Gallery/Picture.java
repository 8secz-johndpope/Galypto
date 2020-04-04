package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.PreviewChache;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;

public class Picture extends Media {

    public Picture(String path) {
        this.path = path;
    }

    public void accept(MediaVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public File store(String sharingPath) {
        File path = new File(sharingPath);

        if (!path.exists())
            path.mkdir();

        File toShare = new File(sharingPath, filename + ".jpg");

        for (int i = 0; toShare.exists(); i++)
            toShare = new File(sharingPath, filename + i + ".jpg");

        return FilesManager.copy(getFullPath(), toShare.getPath());
    }

    /**
     * Returns the scale to which reduce the preview quality in order to save space.
     * @return Final quality percentage.
     */
    private double getDiscount() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap preview = BitmapFactory.decodeFile(getFullPath(), options);

        double discount = 0.75;

        int value = Math.min(preview.getHeight(), preview.getWidth());

        if (discount * value > 370) {
            while (discount * value > 370)
                discount = discount * 0.5;
        }
        else if (discount * value < 340) {

            while (discount * value <= 340 && discount < 1)
                discount = discount + 0.25;
        }

        return discount;
    }
    
    public String getFullPath() {
        return super.getFullPath() + ".jpg";
    }

    protected void findPreview() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap preview = BitmapFactory.decodeFile(getFullPath(), options);
        double discount = getDiscount();
        preview = Bitmap.createScaledBitmap(preview, (int)(preview.getWidth()*discount),
                                            (int)(preview.getHeight()*discount), false);

        PreviewChache.getInstance().addPreview(this, preview);
    }
}
