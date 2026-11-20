package recordshop;

import recordshop.model.InMemoryRecordRepository;
import recordshop.model.Record;
import recordshop.model.RecordRepository;
import recordshop.model.SqliteRecordRepository;

import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        // RecordRepository repo = new InMemoryRecordRepository();
        RecordRepository repo = new SqliteRecordRepository("records.db");

        runInteractiveMenu(repo);
    }

    private static void runInteractiveMenu(RecordRepository repo) {
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n1) Add  2) Edit  3) Delete  4) List  5) Exit");
            switch (input.nextLine().trim()) {
                case "1" -> addRecordFlow(input, repo);
                case "2" -> editRecordFlow(input, repo);
                case "3" -> deleteRecordFlow(input, repo);
                case "4" -> printAll(repo);
                case "5" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        input.close();
    }

    private static void addRecordFlow(Scanner input, RecordRepository repo) {
        try {
            System.out.print("Title: ");
            String title = input.nextLine();
            System.out.print("Artist: ");
            String artist = input.nextLine();
            System.out.print("In stock (Y/N): ");
            boolean instock = input.nextLine().trim().equalsIgnoreCase("Y");
            System.out.print("Price: ");
            double price = Double.parseDouble(input.nextLine().trim());

            Record created = repo.save(new Record(price, artist, instock, title));
            System.out.println("Added: " + created);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format.");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    private static void editRecordFlow(Scanner input, RecordRepository repo) {
        printAll(repo);
        System.out.print("Enter ID to edit: ");
        Record existing = repo.findById(input.nextLine().trim());
        if (existing == null) {
            System.out.println("Record not found.");
            return;
        }
        try {
            System.out.print("New title (blank to keep): ");
            String title = input.nextLine();
            if (!title.trim().isEmpty()) existing.setTitle(title);

            System.out.print("New artist (blank to keep): ");
            String artist = input.nextLine();
            if (!artist.trim().isEmpty()) existing.setArtist(artist);

            System.out.print("New price (blank to keep): ");
            String priceStr = input.nextLine().trim();
            if (!priceStr.isEmpty()) existing.setPrice(Double.parseDouble(priceStr));

            System.out.print("In stock? (Y/N or blank to keep): ");
            String stockStr = input.nextLine().trim();
            if (stockStr.equalsIgnoreCase("Y")) existing.setAvailability(true);
            else if (stockStr.equalsIgnoreCase("N")) existing.setAvailability(false);

            repo.save(existing);
            System.out.println("Updated: " + repo.findById(existing.getIdnumber()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format.");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    private static void deleteRecordFlow(Scanner input, RecordRepository repo) {
        printAll(repo);
        System.out.print("Enter ID to delete: ");
        String id = input.nextLine().trim();
        if (repo.findById(id) == null) {
            System.out.println("Record not found.");
            return;
        }
        repo.deleteById(id);
        System.out.println("Deleted #" + id);
    }

    private static void printAll(RecordRepository repo) {
        List<Record> all = repo.findAll();
        if (all.isEmpty()) {
            System.out.println("(no records)");
            return;
        }
        System.out.println(String.format("%-4s %-28s %-24s %8s  %-12s",
                "ID", "TITLE", "ARTIST", "PRICE", "AVAILABILITY"));
        for (Record r : all) {
            System.out.println(r);
        }
        System.out.println("(" + all.size() + " records)");
    }
}