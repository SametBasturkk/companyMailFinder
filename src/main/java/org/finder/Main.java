package org.finder;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int MAX_THREADS = 128;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting the application...");
        SqlLiteUtil.prepareData();

        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        for (int i = 1; i < 11389; i++) {
            final int companyId = i;
            executor.execute(() -> {
                HashMap<String, String> company = SqlLiteUtil.getCompanyById(companyId);

                if (company.get("mail") == null || company.get("mail").equals("not found")) {
                    SqlLiteUtil.updateMail(company.get("name"), BingUtil.searchResultsBingMail(company.get("name")));
                } else {
                    if (company.get("linkedin") == null || company.get("linkedin").equals("not found")) {
                        SqlLiteUtil.updateLinkedin(company.get("name"), WebsiteUtil.websiteLinkedin(company.get("mail").split("@")[1]));
                    }
                }

                if (company.get("location") == null || company.get("location").equals("not found")) {
                    SqlLiteUtil.updateAddress(company.get("name"), BingUtil.searchResultsBingLocation(company.get("name")));
                }


            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        System.out.println("Application finished.");
    }
}