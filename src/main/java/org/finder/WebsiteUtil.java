package org.finder;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebsiteUtil {
    @SneakyThrows
    public static String websiteLinkedin(String domain) {
        Document doc = Jsoup.connect(domain).followRedirects(true).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
        String linkedin = doc.select("a[href*=linkedin.com]").attr("href");
        return linkedin;
    }
}
