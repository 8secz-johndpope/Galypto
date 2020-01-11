package com.example.cripto_photoaffix.Security.EncryptedFiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.cripto_photoaffix.Flatbuffers.FlatBufferBuilder;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncryptedPicture extends EncryptedFile {

    public EncryptedPicture(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedPicture() {
        super();
    }

    public Media accept(EncryptedFileVisitor visitor) {
        return visitor.visit(this);
    }

    public String decrypt(String password) {
        MyEncryptor encryptor = MyEncryptor.getInstance();
        String data = encryptor.decrypt(this, password);

        Bitmap bitmap = stringToBitmap(data);
        String pth = path + "/" + fileName;
        File file = new File(pth + ".jpg");

        if (!file.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(pth + ".jpg");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pth;
    }

    private Bitmap stringToBitmap(String bit) {
        byte[] bytes = Base64.decode(bit, Base64.DEFAULT);

        Bitmap res = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        bytes = null;

        return res;
    }

    public EncryptedFile clone() {
        return new EncryptedPicture(data, salt, iv, fileName, path);
    }

    public FlatBufferBuilder serialize() {
        assert data != null && iv != null && salt != null && fileName != null && path != null;
        FlatBufferBuilder builder = new FlatBufferBuilder();

        int filename = builder.createString(fileName);
        int pth = builder.createString(path);
        int data = builder.createByteVector(this.data);
        int salt = builder.createByteVector(this.salt);
        int iv = builder.createByteVector(this.iv);
        int type = builder.createString("image");

        EncryptedFileFBS.startEncryptedFileFBS(builder);

        EncryptedFileFBS.addData(builder, data);
        EncryptedFileFBS.addSalt(builder, salt);
        EncryptedFileFBS.addIv(builder, iv);
        EncryptedFileFBS.addFilename(builder, filename);
        EncryptedFileFBS.addPath(builder, pth);
        EncryptedFileFBS.addType(builder, type);

        int created = EncryptedFileFBS.endEncryptedFileFBS(builder);

        builder.finish(created);

        return builder;
    }
}
