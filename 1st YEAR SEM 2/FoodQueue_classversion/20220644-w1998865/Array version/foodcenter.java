import java.util.*;
import java.io.*;
public class foodcenter {
    private static final int MAX_BURGERS = 50;
    private static final int WARNING_STOCK = 10;
    private static final int[] MAX_CUSTOMERS_PER_QUEUE = {2, 3, 5};

    private static Queue<String>[] queues;
    private static int burgerStock;//number of burgers in stock

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        initializeQueues();
        burgerStock = MAX_BURGERS;

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 100:
                case 1000:
                    viewAllQueues();
                    break;
                case 101:
                case 1001:
                    viewEmptyQueues();
                    break;
                case 102:
                case 1002:
                    addCustomerToQueue(scanner);
                    break;
                case 103:
                case 1003:
                    removeCustomerFromQueue(scanner);
                    break;
                case 104:
                case 1004:
                    removeServedCustomer(scanner);
                    break;
                case 105:
                case 1005:
                    viewCustomersSorted();
                    break;
                case 106:
                case 1006:
                    storeProgramData();
                    break;
                case 107:
                case 1007:
                    loadProgramData();
                    break;
                case 108:
                case 1008:
                    viewRemainingBurgerStock();
                    break;
                case 109:
                case 1009:
                    addBurgersToStock(scanner);
                    break;
                case 999:
                case 1999:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 999 && choice != 1999);

        scanner.close();
    }

    @SuppressWarnings("unchecked")
    private static void initializeQueues() { //initializes the array of queues
        queues = new Queue[MAX_CUSTOMERS_PER_QUEUE.length];
        for (int i = 0; i < MAX_CUSTOMERS_PER_QUEUE.length; i++) {
            queues[i] = new LinkedList<>();
        }
    }

    private static void displayMenu() {
        System.out.println("\n*****************");
        System.out.println("   * Cashiers *");
        System.out.println("*****************");
        System.out.println("X         X         X");
        System.out.println("X         X         X");
        System.out.println("          X         X");
        System.out.println("                    X");
        System.out.println("                    X");
        System.out.println("\nMenu Options:");
        System.out.println("100 or VFQ: View all Queues");
        System.out.println("101 or VEQ: View all Empty Queues");
        System.out.println("102 or ACQ: Add customer to a Queue");
        System.out.println("103 or RCQ: Remove a customer from a Queue");
        System.out.println("104 or PCQ: Remove a served customer");
        System.out.println("105 or VCS: View Customers Sorted in alphabetical order");
        System.out.println("106 or SPD: Store Program Data into file");
        System.out.println("107 or LPD: Load Program Data from file");
        System.out.println("108 or STK: View Remaining burgers Stock");
        System.out.println("109 or AFS: Add burgers to Stock");
        System.out.println("999 or EXT: Exit the Program");
        System.out.print("Enter your choice: ");
    }

    private static void viewAllQueues() {
        System.out.println("\n***********");
        System.out.println("   Queues");
        System.out.println("***********");
        for (int i = 0; i < queues.length; i++) {
            Queue<String> queue = queues[i];
            if (queue.isEmpty()) {
                System.out.println("Queue " + (i + 1) + ": O");
            } else {
                System.out.println("Queue " + (i + 1) + ": X");
            }
        }
    }

    private static void viewEmptyQueues() {
        System.out.println("\n***************");
        System.out.println("   Empty Queues");
        System.out.println("***************");
        for (int i = 0; i < queues.length; i++) {
            Queue<String> queue = queues[i];
            if (queue.isEmpty()) {
                System.out.println("Queue " + (i + 1) + ": O");
            }
        }
    }

    private static void addCustomerToQueue(Scanner scanner) {
        System.out.print("Enter the name of the customer: ");
        String name = scanner.nextLine();

        System.out.print("Enter the queue number (1-" + queues.length + "): ");
        int queueNumber = scanner.nextInt();

        if (queueNumber >= 1 && queueNumber <= queues.length) {
            Queue<String> queue = queues[queueNumber - 1];
            if (queue.size() < MAX_CUSTOMERS_PER_QUEUE[queueNumber - 1]) {
                queue.offer(name);
                burgerStock -= 5;

                if (burgerStock <= WARNING_STOCK) {
                    System.out.println("Warning: Low burger stock (" + burgerStock + " burgers remaining).");
                }
                System.out.println("Customer added to Queue " + queueNumber);
            } else {
                System.out.println("Queue " + queueNumber + " is full. Customer cannot be added.");
            }
        } else {
            System.out.println("Invalid queue number. Customer cannot be added.");
        }
        scanner.nextLine(); // Consume newline character
    }

    private static void removeCustomerFromQueue(Scanner scanner) {
        System.out.print("Enter the queue number (1-" + queues.length + "): ");
        int queueNumber = scanner.nextInt();

        if (queueNumber >= 1 && queueNumber <= queues.length) {
            Queue<String> queue = queues[queueNumber - 1];
            if (!queue.isEmpty()) {
                System.out.print("Enter the name of the customer to remove: ");
                scanner.nextLine(); // Consume newline character
                String customerName = scanner.nextLine();

                boolean customerRemoved = false;
                Iterator<String> iterator = queue.iterator();
                while (iterator.hasNext()) {
                    String currentCustomer = iterator.next();
                    if (currentCustomer.equalsIgnoreCase(customerName)) {
                        iterator.remove();
                        customerRemoved = true;
                        break;
                    }
                }

                if (customerRemoved) {
                    System.out.println("Customer removed from Queue " + queueNumber + ": " + customerName);
                } else {
                    System.out.println("The customer '" + customerName + "' was not found in Queue " + queueNumber);
                }
            } else {
                System.out.println("Queue " + queueNumber + " is empty. No customer to remove.");
            }
        } else {
            System.out.println("Invalid queue number. No customer removed.");
        }
    }

    private static void removeServedCustomer(Scanner scanner) {
        System.out.print("Enter the queue number (1-" + queues.length + "): ");
        int queueNumber = scanner.nextInt();

        if (queueNumber >= 1 && queueNumber <= queues.length) {
            Queue<String> queue = queues[queueNumber - 1];
            if (!queue.isEmpty()) {
                System.out.print("Enter the name of the customer to remove: ");
                scanner.nextLine(); // Consume newline character
                String customerName = scanner.nextLine();

                boolean customerRemoved = false;
                Iterator<String> iterator = queue.iterator();
                while (iterator.hasNext()) {
                    String currentCustomer = iterator.next();
                    if (currentCustomer.equalsIgnoreCase(customerName)) {
                        iterator.remove();
                        customerRemoved = true;
                        break;
                    }
                }

                if (customerRemoved) {
                    System.out.println("Customer removed from Queue " + queueNumber + ": " + customerName);
                } else {
                    System.out.println("The customer '" + customerName + "' was not found in Queue " + queueNumber);
                }
            } else {
                System.out.println("Queue " + queueNumber + " is empty. No customer to remove.");
            }
        } else {
            System.out.println("Invalid queue number. No customer removed.");
        }
    }

    private static void viewCustomersSorted() {
        System.out.println("\n*******************************");
        System.out.println("   Customers Sorted Alphabetically by Queue");
        System.out.println("*******************************");

        for (int i = 0; i < queues.length; i++) {
            Queue<String> queue = queues[i];
            String[] customers = queue.toArray(new String[queue.size()]);
            sortArray(customers);//sorts an array of strings in alphabetical order using the bubble sort algorithm

            System.out.println("Queue " + (i + 1) + ":");
            for (String customer : customers) {
                System.out.println(customer);
            }
            System.out.println();
        }
    }

    private static void sortArray(String[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j].compareToIgnoreCase(arr[j + 1]) > 0) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    private static void storeProgramData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("program_data.txt");
            PrintWriter printWriter = new PrintWriter(fileOutputStream);

            for (int i = 0; i < queues.length; i++) {
                Queue<String> queue = queues[i];
                printWriter.print("Queue " + (i + 1) + ": ");
                if (queue.isEmpty()) {
                    printWriter.println("O");
                } else {
                    while (!queue.isEmpty()) {
                        printWriter.print(queue.poll());
                        if (!queue.isEmpty()) {
                            printWriter.print(", ");
                        }
                    }
                    printWriter.println();
                }
            }

            printWriter.close();
            fileOutputStream.close();
            System.out.println("Program data stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing program data: " + e.getMessage());
        }
    }

    private static void loadProgramData() {
        try {
            FileInputStream fileInputStream = new FileInputStream("program_data.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            for (int i = 0; i < queues.length; i++) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    String[] queueData = line.split(":");
                    if (queueData.length == 2) {
                        Queue<String> queue = queues[i];
                        String[] customers = queueData[1].trim().split(",");
                        for (String customer : customers) {
                            queue.offer(customer.trim());
                        }
                    }
                }
            }

            bufferedReader.close();
            fileInputStream.close();
            System.out.println("Program data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Program data file not found.");
        } catch (IOException e) {
            System.out.println("Error loading program data: " + e.getMessage());
        }
    }

    private static void viewRemainingBurgerStock() {
        System.out.println("\n***********************");
        System.out.println("   Remaining Burger Stock");
        System.out.println("***********************");
        System.out.println("Burger stock: " + burgerStock + " burgers");
    }

    private static void addBurgersToStock(Scanner scanner) {
        System.out.print("Enter the number of burgers to add: ");
        int burgersToAdd = scanner.nextInt();

        if (burgersToAdd > 0) {
            burgerStock += burgersToAdd;
            System.out.println(burgersToAdd + " burgers added to stock.");
        } else {
            System.out.println("Invalid number of burgers. No burgers added to stock.");
        }
    }
}
