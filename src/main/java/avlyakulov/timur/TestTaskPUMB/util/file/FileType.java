package avlyakulov.timur.TestTaskPUMB.util.file;

import lombok.Getter;

@Getter
public enum FileType {

    CSV("csv"), XML("xml");

    private final String fileType;

    FileType(String fileType) {
        this.fileType = fileType;
    }
}