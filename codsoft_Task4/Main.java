
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CurrencyTableGenerator {

    private HashMap<String, Double> currencyData = new HashMap<>();

    public CurrencyTableGenerator(String currencyName) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://open.er-api.com/v6/latest/" + currencyName))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String input = response.body();
            String regex = "\"[A-Z]{3}\":[0-9]+\\.?\\d*";
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(input);
            ArrayList<String> matches = new ArrayList<>();
            while (matcher.find()) {
                matches.add(matcher.group());
            }
            if (matches.isEmpty()) {
                System.out.println("No Currency Data found.");
                System.exit(0);
            } else {
                for (Iterator<String> it = matches.iterator(); it.hasNext();) {
                    String[] elem = it.next().replaceAll("\"", "").split(":");
                    currencyData.put(elem[0], Double.parseDouble(elem[1]));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HashMap<String, Double> getCurrencyTable() {
        return currencyData;
    }
}

public class Main {

    static {

        System.out.println("Currency Codes: USD, AED, AFN, ALL, AMD, ANG, AOA, ARS, AUD, AWG "
                + "AZN, BAM, BBD, BDT, BGN, BHD, BIF, BMD, BND, BOB, "
                + "BRL, BSD, BTN, BWP, BYN, BZD, CAD, CDF, CHF, CLP, "
                + "CNY, COP, CRC, CUP, CVE, CZK, DJF, DKK, DOP, DZD, "
                + "EGP, ERN, ETB, EUR, FJD, FKP, FOK, GBP, GEL, GGP, "
                + "GHS, GIP, GMD, GNF, GTQ, GYD, HKD, HNL, HRK, HTG, "
                + "HUF, IDR, ILS, IMP, INR, IQD, IRR, ISK, JEP, JMD, "
                + "JOD, JPY, KES, KGS, KHR, KID, KMF, KRW, KWD, KYD, "
                + "KZT, LAK, LBP, LKR, LRD, LSL, LYD, MAD, MDL, MGA, "
                + "MKD, MMK, MNT, MOP, MRU, MUR, MVR, MWK, MXN, MYR, "
                + "MZN, NAD, NGN, NIO, NOK, NPR, NZD, OMR, PAB, PEN, "
                + "PGK, PHP, PKR, PLN, PYG, QAR, RON, RSD, RUB, RWF, "
                + "SAR, SBD, SCR, SDG, SEK, SGD, SHP, SLE, SLL, SOS, "
                + "SRD, SSP, STN, SYP, SZL, THB, TJS, TMT, TND, TOP, "
                + "TRY, TTD, TVD, TWD, TZS, UAH, UGX, UYU, UZS, VES, "
                + "VND, VUV, WST, XAF, XCD, XDR, XOF, XPF, YER, ZAR, "
                + "ZMW, ZWL");
    }

    public static void main(String[] args) {
        Scanner keypad = new Scanner(System.in);
        System.out.println("Enter Preffered Currency code:");
        String currencyCode1 = keypad.next();
        keypad.nextLine();
        System.out.println("Enter Currency code to convert:");
        String currencyCode2 = keypad.next();
        keypad.nextLine();
        CurrencyTableGenerator currencyTable = new CurrencyTableGenerator(currencyCode1);
        System.err.println("Enter the Amount in" + currencyCode1);
        Double currencyAmount1 = keypad.nextDouble();
        HashMap<String, Double> resultTable = currencyTable.getCurrencyTable();
        Double currencyAmount2 = resultTable.get(currencyCode2);
        if (currencyAmount2 == null) {
            System.out.print("No Currency Data Found for Second Currency Code!");
            System.exit(0);
        } else {
            System.err.println(currencyAmount1 + " " + currencyCode1 + "=" + (currencyAmount1 * currencyAmount2) + " " + currencyCode2);
        }
    }
}
