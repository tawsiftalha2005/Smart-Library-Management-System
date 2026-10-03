package utils;

import java.util.ArrayList;
import java.util.List;

/** Small single-line CSV codec used by the text persistence files. */
public final class CsvFormat {
    private CsvFormat() {}

    public static String encode(String... fields) {
        List<String> encoded = new ArrayList<>(fields.length);
        for (String field : fields) {
            String value = field == null ? "" : field;
            encoded.add("\"" + value.replace("\"", "\"\"").replace("\r", " ").replace("\n", " ") + "\"");
        }
        return String.join(",", encoded);
    }

    public static List<String> decode(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"'); i++;
                } else quoted = !quoted;
            } else if (c == ',' && !quoted) {
                fields.add(field.toString()); field.setLength(0);
            } else field.append(c);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed CSV quote");
        fields.add(field.toString());
        return fields;
    }
}
