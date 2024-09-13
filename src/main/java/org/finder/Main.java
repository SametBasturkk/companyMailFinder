package org.finder;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int MAX_THREADS = 48;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting the application...");
        SqlLiteUtil.prepareData();

        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        for (int i = 1; i < 11389; i++) {
            final int companyId = i;
            executor.execute(() -> {
                HashMap<String, String> company = SqlLiteUtil.getCompanyById(companyId);
              /*

              if (company.get("mail").contains("not found") || company.get("mail") == null) {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("mail", GoogleUtil.searchResultsGoogleMail(company.get("name")));
                    data.put("location", company.get("location"));
                    SqlLiteUtil.update(company.get("location"), data.toString());
                }

               */

                if (company.get("location") == null) {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("mail", company.get("mail"));
                    SqlLiteUtil.update(company.get("name"), data.toString());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        System.out.println("Application finished.");
    }
}