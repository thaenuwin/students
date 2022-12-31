package test.students.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Log4j2
public class ExcelWriter {


    private ExcelWriter() {
    }

    public static void writeSingleSheet(String sheetName, List<List<String>> lines, OutputStream bos) throws IOException {

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {

            Sheet sheet = workbook.createSheet(sheetName);
            int rowIndex = 0;
            for (List<String> line : lines) {
                Row row = sheet.createRow(rowIndex);
                int colIndex = 0;
                for (Object data : line) {
                    writeString(row, colIndex, data);
                    colIndex++;
                }
                rowIndex++;

            }
            workbook.write(bos);
        }
    }


    private static void writeString(Row row, int index, Object obj) {
        Cell cellLabel = row.createCell(index);
        cellLabel.setCellValue(obj != null ? obj.toString() : "");
    }
}
