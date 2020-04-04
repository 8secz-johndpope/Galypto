package com.example.cripto_photoaffix.Gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.cripto_photoaffix.FileManagement.FilesManager;
import com.example.cripto_photoaffix.Visitors.MediaVisitors.MediaVisitor;
import java.io.File;

public class Picture extends Media {
    protected double discount;

    public Picture(String path) {
        this.path = path;

     //   BitmapFactory.Options options = new BitmapFactory.Options();
     //   options.inSampleSize = 5;
      //  preview = BitmapFactory.decodeFile(path + ".jpg", options);
        discount = getDiscount();
      //  preview = Bitmap.createScaledBitmap(preview, (int)(preview.getWidth()*d), (int)(preview.getHeight()*d), false);
    }

    public Bitmap getPreview() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap preview = BitmapFactory.decodeFile(path + ".jpg", options);

        return Bitmap.createScaledBitmap(preview, (int)(preview.getWidth()*discount), (int)(preview.getHeight()*discount), false);
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
     * Retorna el porcentaje por el cual se debe reducir la calidad de la imagen para la vista
     * previa. Esto es para ahorrar espacio en memoria.
     * @return Porcentaje de reduccion de calidad.
     */
    private double getDiscount() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap preview = BitmapFactory.decodeFile(path + ".jpg", options);

        double discount = 0.85;

        int value = Math.min(preview.getHeight(), preview.getWidth());

        if (discount * value > 440) {
            while (discount * value > 440)
                discount = discount * 0.5;
        }
        else if (discount * value < 410) {

            while (discount * value <= 410 && discount < 1)
                discount = discount + 0.25;
        }

        return discount;
    }
    
    public String getFullPath() {
        return super.getFullPath() + ".jpg";
    }
}
