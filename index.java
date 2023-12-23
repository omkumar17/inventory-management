import java.util.*;
import java.io.*;

class Login {
    public static void main(String[] args) {
        Scanner obj = new Scanner(System.in);
        int choice = 0;
        String name = "";
        String pass = "";
        String type = "";
        // boolean flag=false;
        String filePath = "public/users.txt"; // Adjust the file name and path as needed
        String inventoryPath = "public/inventory.txt";
        String orderPath="public/order.txt";
        System.out.println("a. Admin");
        System.out.println("c. Customer");
        System.out.println("Enter your type");
        type = obj.next().substring(0, 1);
        // System.out.println(type);
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
                    boolean flag = validation(name, pass, type, filePath);
                    if (type.equalsIgnoreCase("a")) {
                        inventory(flag, inventoryPath);
                    }
                    else if(type.equalsIgnoreCase("c")){
                        String order=cart(flag,inventoryPath);
                        purchase(order,orderPath,inventoryPath);
                    }
                    break;
                case 2:
                    System.out.println("Enter your name: ");
                    name = obj.next();
                    System.out.print("Enter your password : ");
                    pass = obj.next();
                    boolean flag1 = registration(name, pass, type, filePath);
                    if (flag1 == true) {
                        System.out.println("Login Details");
                        System.out.print("Enter your name :");
                        name = obj.next();
                        System.out.print("Enter your password : ");
                        pass = obj.next();
                        boolean flag3 = validation(name, pass, type, filePath);
                        if (type.equalsIgnoreCase("a")) {
                            inventory(flag3, inventoryPath);
                        }
                        else if(type.equalsIgnoreCase("c")){
                        String order1=cart(flag3,inventoryPath);
                        purchase(order1,orderPath,inventoryPath);
                    }
                    }
                    break;
                default:
                    System.out.println("Enter valid choice");
                    break;
            }
        } while (choice != 1 && choice != 2);
        obj.close();
    }

    public static boolean validation(String name, String pass, String type, String filePath) {
        String line = "";
        boolean flag = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String detail[] = line.split(" ");
                if (detail.length == 3 && detail[0].equals(name) && detail[1].equals(pass) && detail[2].equals(type)) {
                    System.out.println("\nwelcome " + detail[0] + "\n");
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

    public static boolean registration(String name, String pass, String type, String filePath) {
        boolean flag = true;

        try (BufferedWriter br = new BufferedWriter(new FileWriter(filePath, true))) {
            // Get user details
            String username = name;
            String password = pass;

            // Write user details to the file
            if (type.equals("a")) {
                System.out.println("please contact admin");
            } else if (type.equals("c")) {
                br.write(username + " " + password + " " + type);
                br.newLine(); // Add a newline to separate entries

                System.out.println("User added successfully.");
            }

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
            String id = "", head = "", edit = "";
            do {
                System.out.println("1. View inventory details");
                System.out.println("2. Enter product into inventory");
                System.out.println("3. Edit file");
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
                    case 3:
                        view(inventoryPath);
                        System.out.println("\n\nEnter the id which you want to update");
                        id = ob.next();
                        System.out.println("Enter the column name which you want to update");
                        head = ob.next();
                        System.out.println("Enter the new value you want");
                        System.out.println("note: if column is pqty then the entered value\nwill be subtracted");
                        edit = ob.next();
                        update(inventoryPath, id, head, edit);
                        System.out.println("\n\nUpdated list");
                        view(inventoryPath);
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } while (ch != 1 && ch != 2 && ch != 3);
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
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }

    }

    public static void update(String inventoryPath, String id, String head, String edit) {
        try (BufferedReader br = new BufferedReader(new FileReader(inventoryPath))) {
            StringBuilder content = new StringBuilder();
            String line;
            String detail[];
            while ((line = br.readLine()) != null) {
                detail = line.split(" ");
                if (detail.length == 3 && detail[0].equals(id) && detail[1].equals(head)) {
                    if(head.equals("pqty")){
                        detail[2]=Integer.toString((Integer.parseInt(detail[2]))-(Integer.parseInt(edit)));
                    }
                    else{
                    detail[2] = edit;
                    }
                    line = detail[0] + " " + detail[1] + " " + detail[2];
                    
                }
                content.append(line).append("\n");
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(inventoryPath));
            bw.write(content.toString());
            bw.close();

            System.out.println("File has been successfully edited.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static String cart(boolean flag,String inventoryPath){
        String cart="";
        if(flag==true){
        Scanner ob = new Scanner(System.in);
         String line = "";
         String ans="";
         
        // int ctr = 1;
        String detail[];
            try (RandomAccessFile br = new RandomAccessFile(inventoryPath,"r")) {
                view(inventoryPath);
                System.out.println("\n\nDo you want to purchase");
                ans=ob.next().substring(0,1);
                if(ans.equals("y")){
                    ans="";
                    do{
                        System.out.println("\nAdd items to cart");
                        System.out.println("Enter Product id (PID)");
                        String id=ob.next();
                        System.out.println("Enter product quantity");
                        int qty=ob.nextInt();
                        String price="";
                        while ((line = br.readLine()) != null) {
                            detail = line.split(" ");
                            if(detail.length==3 && detail[0].equals(id) && detail[1].equals("pqty")){
                                if(qty>Integer.parseInt(detail[2])){
                                    System.out.println("Quantity entered is not available.\nPlease enter quantity below "+detail[2]);
                                    qty=ob.nextInt();
                                    System.out.println("Added to cart");
                                }
                                else{
                                    System.out.println("Added to cart");
                                }
                            }
                            if(detail.length==3 && detail[0].equals(id) && detail[1].equals("pprice")){
                                price=detail[2];
                            }
                        }
                        cart=cart+id+" "+qty+" "+price+" ";
                        br.seek(0);
                        System.out.println("Do you want to add more items(y/n)");
                        ans=ob.next().substring(0,1);
                    }while(ans.equals("y"));
                 }
                 br.close();
                ob.close();
            }catch (IOException e) {
                e.printStackTrace();
                System.out.println("An error occurred while reading the file: " + e.getMessage());
            }
        }
        return cart;
    }
    public static void purchase(String order,String orderPath,String inventoryPath){
        String detail[];
        int price=0;
        Scanner ob = new Scanner(System.in);
        try (BufferedWriter br = new BufferedWriter(new FileWriter(orderPath, true))) {
            detail=order.split(" ");
            for (int i = 0; i < detail.length; i=i+3) {
                br.write(detail[i] + " "+ detail[i+1]+" "+detail[i+2]);
                price=price+((Integer.parseInt(detail[i+1]))*(Integer.parseInt(detail[i+2])));
                update(inventoryPath, detail[i],"pqty",detail[i+1] );
                br.newLine();  
            }
            br.close();
            System.out.println("Order successful");
            System.out.println("Your total order value is : "+price);
            System.out.println("Thank you for shopping!");

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
        ob.close();
    }

}
