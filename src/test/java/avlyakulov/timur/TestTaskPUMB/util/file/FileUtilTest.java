package avlyakulov.timur.TestTaskPUMB.util.file;

import avlyakulov.timur.TestTaskPUMB.exception.FileIsEmptyException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileUtilTest {

    private FileUtil fileUtil;

    private MultipartFile fileCsv;

    private MultipartFile fileXml;

    @BeforeEach
    void setUp() {
        fileUtil = new FileUtil();
    }

    @Test
    void getFileType_fileTypeIsCsv_fileIsValid() {
        fileCsv = new MockMultipartFile("animals", "animals.csv", "text/csv", "content".getBytes());

        FileType fileType = fileUtil.getFileType(fileCsv);
        assertThat(fileType).isEqualTo(FileType.CSV);
    }

    @Test
    void getFileType_fileTypeIsXml_fileIsValid() {
        fileXml = new MockMultipartFile("animals", "animals.xml", "application/xml", "content".getBytes());

        FileType fileType = fileUtil.getFileType(fileXml);
        assertThat(fileType).isEqualTo(FileType.XML);
    }

    @Test
    void getFileType_fileTypeIsCsvAndFileIsNull_throwsException() {
        fileXml = null;

        assertThrows(FileIsEmptyException.class, () -> fileUtil.getFileType(fileXml));
    }

    @Test
    void getFileType_fileTypeIsNotValid_throwsException() {
        fileXml = new MockMultipartFile("animals", "animals.pdf", "application/pdf", "content".getBytes());

        assertThrows(FileNotSupportedException.class, () -> fileUtil.getFileType(fileXml));
    }

    @Test
    void getFileType_fileTypeIsCsvAndFileIsEmpty_throwsException() {
        fileXml = fileCsv = new MockMultipartFile("animals", "animals.csv", "text/csv", "".getBytes());

        assertThrows(FileIsEmptyException.class, () -> fileUtil.getFileType(fileXml));
    }
}