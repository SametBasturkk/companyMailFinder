package org.finder;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting the application...");
        SqlLiteUtil.prepareData();

        HashMap<String, String> company = SqlLiteUtil.getCompanyById(1);


    }
}