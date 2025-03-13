
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class PasswordManager {

    private static final HashMap<String, String> passwordStore = new HashMap<>();
    private static SecretKey secretKey;

    public static void main(String[] args) throws Exception {
        secretKey = generateKey();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printPikachuMenu();
            int choice = getValidChoice(scanner);

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter website: ");
                    String website = scanner.nextLine().trim();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine().trim();
                    String encryptedPassword = encrypt(password, secretKey);
                    passwordStore.put(website, encryptedPassword);
                    System.out.println("✅ Password stored successfully!");
                }

                case 2 -> {
                    System.out.print("Enter website: ");
                    String website = scanner.nextLine().trim();
                    if (passwordStore.containsKey(website)) {
                        String decryptedPassword = decrypt(passwordStore.get(website), secretKey);
                        System.out.println("🔓 Password for " + website + ": " + decryptedPassword);
                    } else {
                        System.out.println("❌ No password found for this website.");
                    }
                }

                case 3 -> {
                    if (passwordStore.isEmpty()) {
                        System.out.println("📭 No stored passwords.");
                    } else {
                        System.out.println("\n📜 Stored Passwords:");
                        for (String site : passwordStore.keySet()) {
                            String decryptedPassword = decrypt(passwordStore.get(site), secretKey);
                            System.out.println("🔑 " + site + " ➝ " + decryptedPassword);
                        }
                    }
                }

                case 4 -> {
                    System.out.println("⚡ Pika Pika! Bye! ⚡");
                    scanner.close();
                    return;
                }

                default ->
                    System.out.println("❗ Invalid option. Choose between 1-4.");
            }
        }
    }

    private static void printPikachuMenu() {
        System.out.println("\n******************************************");
        System.out.println("*      ⚡ PIKACHU PASSWORD MANAGER ⚡   **");
        System.out.println("****************************************");
        System.out.println("*        (\\__/)                         **");
        System.out.println("*         (o^.^) 🔥   PIKA PIKA!        **");
        System.out.println("*         (>🍪)           Coded by Eddy **");
        System.out.println("****************************************");
        System.out.println("* 1. Store Password 🗝️                    **");
        System.out.println("* 2. Retrieve Password 🔍                **");
        System.out.println("* 3. View All Stored Passwords 📜        **");
        System.out.println("* 4. Exit 🚪                             **");
        System.out.println("****************************************");
        System.out.print(  "Choose an option: ");
    }

    private static int getValidChoice(Scanner scanner) {
        while (true) {
            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Invalid input. Enter a number between 1-4.");
                scanner.next();
                System.out.print("Choose an option: ");
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice >= 1 && choice <= 4) {
                return choice;
            }
            System.out.println("❗ Invalid option. Choose between 1-4.");
            System.out.print("Choose an option: ");
        }
    }

    private static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    private static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}
