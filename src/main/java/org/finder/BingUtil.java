package org.finder;

import generator.RandomUserAgentGenerator;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BingUtil {

    private static Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+");

    static List<String> NETHERLANDS_CITIES = Arrays.asList(
            "Tilburg", "Amsterdam", "Rotterdam", "The Hague", "Utrecht", "Maastricht", "Eindhoven", "Groningen",
            "Almere", "Breda", "Nijmegen", "Arnhem", "Haarlem", "Enschede", "’s-Hertogenbosch", "Amersfoort",
            "Zaanstad", "Apeldoorn", "Zwolle", "Zoetermeer", "Leeuwarden", "Leiden", "Dordrecht", "Alphen aan den Rijn",
            "Alkmaar", "Delft", "Emmen", "Deventer", "Helmond", "Hilversum", "Heerlen", "Lelystad", "Purmerend",
            "Hengelo", "Schiedam", "Zaandam", "Hoofddorp", "Vlaardingen", "Gouda", "Hoorn", "Almelo", "Spijkenisse",
            "Amstelveen", "Assen", "Velsen-Zuid", "Capelle aan den IJssel", "Veenendaal", "Katwijk", "Zeist",
            "Nieuwegein", "Scheveningen", "Heerhugowaard", "Roermond", "Oosterhout", "Rijswijk", "Houten",
            "Middelburg", "Harderwijk", "Barendrecht", "IJmuiden", "Zutphen", "Soest", "Ridderkerk", "Schagen",
            "Veldhoven", "Kerkrade", "Zwijndrecht", "Zevenaar", "Noordwijk", "Etten-Leur", "Tiel", "Beverwijk",
            "Huizen", "Hellevoetsluis", "Maarssen", "Wageningen", "Heemskerk", "Veghel", "Teijlingen", "Venlo",
            "Gorinchem", "Landgraaf", "Sittard", "Hoogvliet", "Maassluis", "Bussum", "Papendrecht", "Aalsmeer",
            "Oldenzaal", "Vught", "Nieuw-Vennep", "Waddinxveen", "Diemen", "Hendrik-Ido-Ambacht", "Rosmalen",
            "Best", "Uithoorn", "Krimpen aan den IJssel", "Culemborg", "Geldrop", "Langedijk", "Vleuten",
            "Brunssum", "Heemstede", "Leiderdorp", "Blerick", "Pijnacker", "Dongen", "Voorschoten", "Sliedrecht",
            "Oegstgeest", "Stein", "Oud-Beijerland", "Heiloo", "Borne", "Lisse", "Volendam", "Hillegom",
            "’s-Gravenzande", "De Meern", "Nuenen", "Alblasserdam", "Weesp", "Nootdorp", "Krommenie", "Naaldwijk",
            "Edam", "Enkhuizen", "Hardinxveld-Giessendam", "Waalre", "Rijen", "Glanerbrug", "Schaesberg", "Beek",
            "Boskoop", "Westervoort", "Sassenheim", "Julianadorp", "Badhoevedorp", "Raamsdonksveer", "Rozenburg",
            "Blaricum", "Schoonhoven", "Laren", "Koog aan de Zaan", "Doesburg", "Hoogland", "Leidschendam",
            "Heerlerbaan", "Nieuw-Lekkerland", "Kudelstaart", "Zaandijk", "Den Hoorn", "Zwanenburg", "Limmen",
            "Honselersdijk", "Bolnes", "Santpoort-Noord", "Soesterberg", "Elsloo", "Valkenburg", "Reuver",
            "Surhuisterveen", "Susteren", "Hintham", "Belfeld", "Meteren", "Westerblokker", "Duivendrecht",
            "Empel", "Arnemuiden", "Hooglanderveen", "Pernis", "Soestdijk", "Aerdenhout", "Munstergeleen",
            "Den Dolder", "Rijsenhout", "Overveen", "Molenhoek", "De Kwakel", "Kwintsheul", "Heelsum",
            "Nijkerkerveen", "Santpoort-Zuid", "Goutum", "Nieuwstadt", "Zwaag", "Berg", "Bovenkerk", "Oerle",
            "Elden", "Giesbeek", "Boekelo", "Vlijmen", "Bemmel", "Kralingse Veer", "Born", "Neerbeek", "Mijdrecht",
            "Hoogkarspel", "Bovenkarspel", "Zuid-Scharwoude", "Poortugaal", "Odijk"
    );

    @SneakyThrows
    public static String searchResultsBingMail(String company) {
        String mail = bingDirectlySearchMail(company);
        return mail;
    }

    @SneakyThrows
    public static String searchResultsBingLocation(String company) {
        String location = bingDirectlySearchLocation(company);
        return location;
    }

    private static String bingDirectlySearchMail(String company) throws IOException {
        System.out.println("Searching for company: " + company);
        Document doc = Jsoup.connect("https://www.bing.com/search?q=" + URLEncoder.encode(company + "  mail address", "UTF-8")).userAgent(RandomUserAgentGenerator.getNextNonMobile()).get();

        String extractedEmail = extractEmail(doc.body().text());
        return extractedEmail;
    }

    private static String bingDirectlySearchLocation(String company) throws IOException {
        System.out.println("Searching for company: " + company);
        Document doc = Jsoup.connect("https://www.bing.com/search?q=" + URLEncoder.encode(company + " netherlands location", "UTF-8"))
                .userAgent(RandomUserAgentGenerator.getNextNonMobile()).get();

        String pageContent = doc.body().text().toLowerCase();

        for (String city : NETHERLANDS_CITIES) {
            if (pageContent.contains(city.toLowerCase())) {
                return city;
            }
        }

        return "not found";
    }

    private static String extractEmail(String text) {
        Matcher matcher = emailPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "not found";
        }
    }
}