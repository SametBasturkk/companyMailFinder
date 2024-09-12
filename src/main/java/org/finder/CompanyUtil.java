package org.finder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CompanyUtil {

    public static ArrayList<String> readFromFile() throws FileNotFoundException {
        ArrayList<String> companies = new ArrayList<>();
        File file = new File("src/main/resources/companyList.txt");
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            companies.add(reader.nextLine());
        }
        reader.close();
        return companies;
    }


}
