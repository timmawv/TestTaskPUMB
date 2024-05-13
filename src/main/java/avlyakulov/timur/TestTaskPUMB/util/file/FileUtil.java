package avlyakulov.timur.TestTaskPUMB.util.file;

import avlyakulov.timur.TestTaskPUMB.exception.FileIsEmptyException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public FileType getFileType(MultipartFile file) {
        if (file == null)
            throw new FileIsEmptyException("You didn't attach any file.");

        if (file.isEmpty())
            throw new FileIsEmptyException("Your file is empty. Please send valid files with values.");

        return defineFileType(file);
    }

    private FileType defineFileType(MultipartFile file) {
        String fileType = splitFileTypeFromFile(file);
        switch (fileType) {
            case "csv" -> {
                return FileType.CSV;
            }
            case "xml" -> {
                return FileType.XML;
            }
            default -> throw new FileNotSupportedException("Our application supports only csv and xml files.");
        }
    }

    private String splitFileTypeFromFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String[] fileType = originalFilename.split("\\.");
        return fileType[1];
    }
}