package com.example.cripto_photoaffix.Security.EncryptedFiles;

import android.util.Base64;
import com.example.cripto_photoaffix.Flatbuffers.FlatBufferBuilder;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Security.MyEncryptor;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;
import java.io.File;
import java.io.FileOutputStream;

public class EncryptedVideo extends EncryptedFile {

    public EncryptedVideo(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedVideo() {
        super();
    }

    public String decrypt(String password) {
        String finalPath = path + "/" + fileName;
        File file = new File(finalPath + ".mp4");

        if (!file.exists()) {
            try {

                MyEncryptor encryptor = MyEncryptor.getInstance();

                String data = encryptor.decrypt(this, password);
                byte[] bytes = Base64.decode(data, Base64.DEFAULT);

                FileOutputStream fos = new FileOutputStream(finalPath + ".mp4");

                fos.write(bytes);
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
                finalPath = null;
            }
        }

        return finalPath;
    }

    public Media accept(EncryptedFileVisitor visitor) {
        return visitor.visit(this);
    }

    public EncryptedFile clone() {
        return new EncryptedVideo(data, salt, iv, fileName, path);
    }

    public FlatBufferBuilder serialize() {
        assert data != null && iv != null && salt != null && fileName != null && path != null;
        FlatBufferBuilder builder = new FlatBufferBuilder();

        int filename = builder.createString(fileName);
        int pth = builder.createString(path);
        int data = builder.createByteVector(this.data);
        int salt = builder.createByteVector(this.salt);
        int iv = builder.createByteVector(this.iv);
        int type = builder.createString("video");

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
