package com.example.cripto_photoaffix.Security.EncryptedFiles;

import com.example.cripto_photoaffix.Flatbuffers.FlatBufferBuilder;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.Visitors.EncryptedFilesVisitors.EncryptedFileVisitor;

public class EncryptedPassword extends EncryptedFile {

    public EncryptedPassword(byte[] data, byte[] salt, byte[] iv, String fileName, String path) {
        super(data, salt, iv, fileName, path);
    }

    public EncryptedPassword() {
        super();
    }

    public Media accept(EncryptedFileVisitor visitor) {
        return null;
    }

    public EncryptedFile clone() {
        return new EncryptedPassword(data, salt, iv, fileName, path);
    }

    public FlatBufferBuilder serialize() {
        assert data != null && iv != null && salt != null && fileName != null && path != null;
        FlatBufferBuilder builder = new FlatBufferBuilder();

        int filename = builder.createString(fileName);
        int pth = builder.createString(path);
        int data = builder.createByteVector(this.data);
        int salt = builder.createByteVector(this.salt);
        int iv = builder.createByteVector(this.iv);
        int type = builder.createString("text");

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
