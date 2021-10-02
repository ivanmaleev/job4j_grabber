package ru.job4j.cache;

import java.util.Scanner;

public class Emulator {
    public static void main(String[] args) {
        System.out.println("Please, set cash directory:");
        Scanner scanner = new Scanner(System.in);
        String dirFileCash = scanner.nextLine();
        CashFabric<String, String> cashFabric = new CashFabric<>();
        AbstractCache<String, String> dirFileCache = cashFabric.createCash("DirFile", dirFileCash);
        while (true) {
            System.out.println("Type 0 to load file to cash");
            System.out.println("Type 1 to load file from cash");
            System.out.println("Type 9 to exit");
            scanner = new Scanner(System.in);
            int i = 0;
            try {
                i = scanner.nextInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i == 9) {
                return;
            }
            System.out.println("Please type filename:");
            scanner = new Scanner(System.in);
            String fileName = scanner.nextLine();
            if (i == 0) {
                dirFileCache.load(fileName);
            } else if (i == 1) {
                dirFileCache.get(fileName);
            } else {
                System.out.println("Incorrect number");
            }
        }

    }
}
