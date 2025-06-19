package vn.devTool.core.base.pojo;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Custom resource to send multipart file from MultipartFile.
 */
public class FormDataFile extends InputStreamResource {

    private final String filename;
    private final long contentLength;

    public FormDataFile(MultipartFile file) throws IOException {
        super(file.getInputStream());
        this.filename = file.getOriginalFilename();
        this.contentLength = file.getSize();
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return this.contentLength;
    }
}
