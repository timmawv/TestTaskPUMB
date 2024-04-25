package avlyakulov.timur.TestTaskPUMB.util.csv_filter;

import com.opencsv.bean.CsvToBeanFilter;

public class CsvEmptyFieldFilter implements CsvToBeanFilter {
    @Override
    public boolean allowLine(String[] fields) {
        for (String field : fields)
            if (field.isBlank())
                return false;
        return true;
    }
}
