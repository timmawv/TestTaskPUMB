package avlyakulov.timur.TestTaskPUMB.util.file;

import avlyakulov.timur.TestTaskPUMB.exception.FileIsEmptyException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static FileType getFileType(MultipartFile file) {
        if (file == null)
            throw new FileIsEmptyException("You didn't attach any file.");

        if (file.isEmpty())
            throw new FileIsEmptyException("Your file is empty. Please send valid files with values.");

        return defineFileType(file);
    }

    private static FileType defineFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(FileType.CSV.getFileType())) {
            return FileType.CSV;
        } else if (fileName.endsWith(FileType.XML.getFileType())) {
            return FileType.XML;
        } else {
            throw new FileNotSupportedException("Our application supports only csv and xml files.");
        }
    }
}