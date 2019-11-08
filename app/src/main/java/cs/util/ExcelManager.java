package cs.util;

import cs.data.DataSet;
import cs.data.DataTable;
import cs.string;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.util.*;

public class ExcelManager {
    public static DataSet readExcelFileToDataSet(File file) {
        DataSet ds = null;
        Workbook book = null;
        try {
            book = Workbook.getWorkbook(file);
            Sheet[] sheets = book.getSheets();
            if (sheets.length > 0) {
                ds = new DataSet();
                for (Sheet sheet : sheets) {
                    int rows = sheet.getRows();
                    int columns = sheet.getColumns();
                    if (rows > 0 && columns > 0) {
                        DataTable dt = new DataTable();
                        ArrayList<String> colnames = new ArrayList<String>();
                        Cell[] cols = sheet.getRow(0);
                        // 列数
                        for (int i = 0; i < columns && i < cols.length; i++) {
                            String value = cols[i].getContents();
                            if (!string.IsNullOrEmpty(value)) {
                                colnames.add(value.trim());
                            }
                        }
                        for (int i = 1; i < rows; i++) {
                            Cell[] cells = sheet.getRow(i);// && cells.length==colnames.size()
                            if ((cells.length != 0 && cells[0] != null && !string.IsNullOrEmpty(cells[0].getContents()))) {
                                Map<String, Object> row = new HashMap<>();
                                for (int j = 0; j < cells.length && j < colnames.size(); j++) {
                                    row.put(colnames.get(j), cells[j].getContents().trim());
                                }
                                dt.rows.add(row);
                            }
                        }
                        ds.tables.add(dt);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (book != null) {
                book.close();
            }
        }
        return ds;
    }

    public static DataTable readExcelFile(File file) {
        DataSet ds = readExcelFileToDataSet(file);
        if (ds != null && ds.tables != null && ds.tables.size() > 0) {
            return ds.getFirstTable();
        }
        return null;
    }
}