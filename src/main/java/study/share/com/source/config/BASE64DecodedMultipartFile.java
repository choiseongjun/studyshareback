package study.share.com.source.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class BASE64DecodedMultipartFile implements MultipartFile {
    private final byte[] imgContent;
    private String originalName;
    private String contenttype;
    private long fileSize;

    public BASE64DecodedMultipartFile(byte[] imgContent,String originalName,String contenttype, long fileSize) {
        this.imgContent = imgContent;
        this.originalName = originalName;
        this.contenttype = contenttype;
        this.fileSize = fileSize;
    }

    @Override
    public String getName() {
        // TODO - implementation depends on your requirements 
        return null;
    }

    @Override
    public String getOriginalFilename() {
        // TODO - implementation depends on your requirements
        return originalName;
    }

    @Override
    public String getContentType() {
        return contenttype;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileSize;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException { 
        new FileOutputStream(dest).write(imgContent);
    }
}