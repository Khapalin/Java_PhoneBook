package homePhonebook;

import java.io.*;
import java.util.*;

public class PhoneBook {
    private HashMap<String, HashSet<String>> phoneBook;

    public PhoneBook() {
        this.phoneBook = new HashMap<>();
    }

    public void addContact(String name, String phone) {
        if (!phoneBook.containsKey(name)) {
            phoneBook.put(name, new HashSet<>());
        }

        boolean contactExists = false;
        Set<String> phones = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/eric/Desktop/Тестировщик/1_Java/Java_PhoneBook/homePhonebook/book.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(name + ":")) {
                    contactExists = true;
                    String[] parts = line.split(":");
                    phones.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        phoneBook.get(name).add(phone);
        phones.add(phone);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/eric/Desktop/Тестировщик/1_Java/Java_PhoneBook/homePhonebook/book.txt"))) {
            for (String contactName : phoneBook.keySet()) {
                Set<String> phoneNumbers = phoneBook.get(contactName);
                for (String phoneNumber : phoneNumbers) {
                    writer.write(contactName + ":" + phoneNumber);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void deleteContact(String name) {
        if (phoneBook.containsKey(name)) {
            phoneBook.remove(name);
            try (BufferedReader reader = new BufferedReader(new FileReader("/Users/eric/Desktop/Тестировщик/1_Java/Java_PhoneBook/homePhonebook/book.txt"));
                 BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/eric/Desktop/Тестировщик/1_Java/Java_PhoneBook/homePhonebook/book_temp.txt "))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(name + ":")) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка: " + e.getMessage());
                return;
            }

            File file = new File("/Users/eric/Desktop/Тестировщик/1_Java/Java_PhoneBook/homePhonebook/book.txt");
            File tempFile = new File("/Users/eric/Desktop/Тестировщик/1_Java/Java_PhoneBook/homePhonebook/book_temp.txt");

            if (!tempFile.exists()) {
                System.out.println("Файл book_temp.txt не существует.");
                return;
            }

            if (file.exists()) {
                file.delete();
            }

            if (tempFile.renameTo(file)) {
                System.out.println("Контакт успешно удален.");
            } else {
                System.out.println("Не удалось переименовать файл.");
            }
        } else {
            System.out.println("Контакт не найден.");
        }
    }


    public void printPhoneBook() {
        List<Map.Entry<String, HashSet<String>>> sortedList = new ArrayList<>(phoneBook.entrySet());
        sortedList.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));

        for (Map.Entry<String, HashSet<String>> entry : sortedList) {
            System.out.println(entry.getKey() + ": " + entry.getValue().size() + " phones");
            for (String phone : entry.getValue()) {
                System.out.println("\t" + phone);
            }
        }
    }

    public static void main(String[] args) {
        PhoneBook myPhoneBook = new PhoneBook();
        String fileName = "/Users/eric/Desktop/Тестировщик/1_Java/Java_PhoneBook/homePhonebook/book.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String name = parts[0];
                String[] phones = parts[1].split(",");
                for (String phone : phones) {
                    myPhoneBook.addContact(name, phone);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить контакт");
            System.out.println("2. Удалить контакт");
            System.out.println("3. Показать контакты");
            System.out.println("4. Выход");
            System.out.print("Введите пункт меню: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Введите номер телефона: ");
                    String phone = scanner.nextLine();
                    myPhoneBook.addContact(name, phone);
                    break;
                case 2:
                    System.out.print("Введите имя для удаления: ");
                    String removeName = scanner.nextLine();
                    System.out.print("Введите номер телефона для удаления: ");
                    String removePhone = scanner.nextLine();
                    myPhoneBook.deleteContact(removeName);
                    break;
                case 3:
                    myPhoneBook.printPhoneBook();
                    break;
                case 4:
                    System.out.println("Досвидания.");
                    System.exit(0);
                default:
                    System.out.println("Такого пункта меню не существует, пожалуйста повторите.");
            }
        }
    }
}