package organizze;

import java.text.NumberFormat;
import java.util.Locale;

public class Util {
    private static Locale ptBr = new Locale("pt", "BR");
    private static NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);

    public static long getAmmount(String value) {
        if (value.contains(",") && value.contains(".")) {
            return Long.parseLong(value.replace(",", "")
                    .replace(".", "").replaceAll("[\\s]",""));
        } else if (value.contains(".")) {
            return Long.parseLong(value.replace(".", "").replaceAll("[\\s]",""));
        } else if (value.contains(",")) {
            return Long.parseLong(value.replace(",", "").replaceAll("[\\s]",""));
        }
        return 0;
    }

    public static String addCommaPointer(String string) {
        string = nf.format(Double.parseDouble(string) / 100)
                .replaceAll("[R]", "")
                .replaceAll("[$]", "").trim();
        return string;
    }

}
