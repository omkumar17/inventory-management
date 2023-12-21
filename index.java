import java.util.*;
import java.io.*;

class Login {
    public static void main(String[] args) {
        Scanner obj = new Scanner(System.in);
        int choice = 0;
        String name = "";
        String pass = "";
        // boolean flag=false;
        String filePath = "public/users.txt"; // Adjust the file name and path as needed
        String inventoryPath = "public/inventory.txt";
        do {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("Enter your choice");
            choice = obj.nextInt();
            obj.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Login Details");
                    System.out.print("Enter your name :");
                    name = obj.next();
                    System.out.print("Enter your password : ");
                    pass = obj.next();
                    boolean flag = validation(name, pass, filePath);
                    inventory(flag, inventoryPath);
                    break;
                case 2:
                    System.out.println("Enter your name: ");
                    name = obj.next();
                    System.out.print("Enter your password : ");
                    pass = obj.next();
                    boolean flag1 = registration(name, pass, filePath);
                    if (flag1 == true) {
                        System.out.println("Login Details");
                        System.out.print("Enter your name :");
                        name = obj.next();
                        System.out.print("Enter your password : ");
                        pass = obj.next();
                        boolean flag3 = validation(name, pass, filePath);
                        inventory(flag3, inventoryPath);
                    }
                    break;
                default:
                    System.out.println("Enter valid choice");
                    break;
            }
        } while (choice != 1 && choice != 2);
        obj.close();
    }

    public static boolean validation(String name, String pass, String filePath) {
        String line = "";
        boolean flag = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String detail[] = line.split(" ");
                if (detail.length == 2 && detail[0].equals(name) && detail[1].equals(pass)) {
                    System.out.println("welcome " + detail[0]);
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                System.out.println("Invalid credentials");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return flag;
    }

    public static boolean registration(String name, String pass, String filePath) {
        boolean flag = true;

        try (BufferedWriter br = new BufferedWriter(new FileWriter(filePath, true))) {
            // Get user details
            String username = name;
            String password = pass;

            // Write user details to the file
            br.write(username + " " + password);
            br.newLine(); // Add a newline to separate entries

            System.out.println("User added successfully.");

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            flag = false;
        }
        return flag;
    }

    public static void inventory(boolean flag, String inventoryPath) {
        if (flag == true) {
            Scanner ob = new Scanner(System.in);
            int ch;
            do {
                System.out.println("1. View inventory details");
                System.out.println("2. Enter product into inventory");
                System.out.println("Enter your choice");
                ch = ob.nextInt();
                ob.nextLine();
                switch (ch) {
                    case 1:
                        view(inventoryPath);
                        break;
                    case 2:
                        insert(inventoryPath);
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } while (ch != 1 && ch != 2);
            ob.close();
        }
    }

    public static void insert(String inventoryPath) {

        String inarr[] = { "pid", "pname", "pprice", "pmfd", "pexp", "pqty" };
        Scanner ob = new Scanner(System.in);
        char ans;
        try (BufferedWriter br = new BufferedWriter(new FileWriter(inventoryPath, true))) {
            do {
                System.out.println("press any key");
                ob.nextLine();
                System.out.println("Enter pid");
                String id = ob.nextLine();
                for (int i = 0; i < inarr.length; i++) {
                    System.out.println("Enter " + inarr[i]);
                    String val = ob.nextLine();
                    br.write(id + " " + inarr[i] + " " + val);
                    br.newLine();
                    System.out.println("Entry successful");
                }
                System.out.println("Wnat to enter more details(y/n)");
                ans = ob.next().charAt(0);
            } while (ans != 'n');
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public static void view(String inventoryPath) {
        String line = "";
        int ctr = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(inventoryPath))) {
            System.out.println("pid\t\tpname\t\tpprice\t\tpmfd\t\tpexp\t\tpqty");
            System.out.println(
                    "--------------------------------------------------------------------------------------");
            while ((line = br.readLine()) != null) {
                String detail[] = line.split(" ");
                if (detail.length == 3 && ctr <= 6) {
                    System.out.print(String.format("%-16s", (detail[2])));
                    ctr++;
                } else {
                    System.out.println();
                    System.out.print(String.format("%-16s", (detail[2])));
                    ctr = 2;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }

    }
}
