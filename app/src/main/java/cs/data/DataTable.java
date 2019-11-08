package cs.data;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;

import cs.string;

public class DataTable {

    public ArrayList<Map<String, Object>> getRows() {
        return rows;
    }

    public ArrayList<Map<String, Object>> rows;

    public DataTable() {
        this.rows = new ArrayList<Map<String, Object>>();
    }

    public Map<String, Object> getFirstRow() {
        if (rows.size() > 0)
            return rows.get(0);
        return null;
    }

    public String getScalar() {
        Map<String, Object> fr = getFirstRow();
        if (fr != null) {
            for (Object obj : fr.keySet()) {
                return fr.get(obj).toString();
            }
        }
        return "";
    }

    public int getIntScalar() {
        String s = getScalar();
        if (string.IsNullOrEmpty(s))
            return 0;
        return Integer.valueOf(s);
    }

    public float getFloatScalar() {
        String s = getScalar();
        if (string.IsNullOrEmpty(s))
            return 0f;
        return Float.valueOf(s);
    }

    public float getDoubleScalar() {
        String s = getScalar();
        if (string.IsNullOrEmpty(s))
            return 0f;
        return Float.valueOf(s);
    }

    public ArrayList<Map<String, Object>> select(Predicate<Map<String, Object>> cmd) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            if (cmd.test(row)) {
                result.add(row);
            }
        }
        return result;
    }
}