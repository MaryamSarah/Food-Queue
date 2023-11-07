import java.util.*;
import java.io.*;
import java.io.Serializable;

public class FoodQueue {
    private static final int MAX_BURGERS = 50;
    private static final int WARNING_STOCK = 10;
    private static final int[] MAX_CUSTOMERS_PER_QUEUE = {2, 3, 5};
    private static final int BURGER_PRICE = 650;
    private static final int WAITING_LIST_SIZE = 10;
    private static Customer[] waitingList;
    private static int waitingListFront;
    private static int waitingListRear;
    private static int waitingListCount;


    private static Queue<Customer>[] queues;
    private static int burgerStock;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        initializeQueues();
        burgerStock = MAX_BURGERS;

        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            displayMenu();
            choice=scanner.nextLine().toUpperCase();

            try{
                int choiceNum=Integer.parseInt(choice);
                processNumericChoice(choiceNum);
            }catch(NumberFormatException e){
                processStringChoice(choice);
            }

            switch (choice) {
                case "100":
                case "VFQ":
                    viewAllQueues();
                    break;
                case "101":
                case "VEQ":
                    viewEmptyQueues();
                    break;
                case "102":
                case "ACQ":
                    addCustomerToQueue();
                    break;
                case "103":
                case "RCQ":
                    removeCustomerFromQueue(scanner);
                    break;
                case "104":
                case "PCQ":
                    removeServedCustomer(scanner);
                    break;
                case "105":
                case "VCS":
                    viewCustomersSorted();
                    break;
                case "106":
                case "SPD":
                    storeProgramData();
                    break;
                case "107":
                case "LPD":
                    loadProgramData();
                    break;
                case "108":
                case "STK":
                    viewRemainingBurgerStock();
                    break;
                case "109":
                case "AFS":
                    addBurgersToStock(scanner);
                    break;
                case "110":
                case "IFQ":
                    viewIncome();
                    break;
                case "999":
                case "EXT":
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("999") && !choice.equals("EXT"));

        scanner.close();
    }

    @SuppressWarnings("unchecked")
    private static void initializeQueues() {
        waitingList = new Customer[WAITING_LIST_SIZE];
        waitingListFront = 0;
        waitingListRear = -1;
        waitingListCount = 0;
        queues = new Queue[MAX_CUSTOMERS_PER_QUEUE.length];
        for (int i = 0; i < MAX_CUSTOMERS_PER_QUEUE.length; i++) {
            queues[i] = new LinkedList<>();
        }

        try{
            FileReader fileReader=new FileReader("customerData.txt");
            BufferedReader bufferedReader=new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String firstName = data[0];
                    String lastName = data[1];
                    int burgersRequired = Integer.parseInt(data[2]);
                    Customer customer = new Customer(firstName, lastName, burgersRequired);

                    int queueIndex = findSmallestQueue();
                    if (queueIndex != -1) {
                        queues[queueIndex].add(customer);
                        System.out.println("Customer " + customer.getFirstName() + " " + customer.getLastName() + " added to queue " + (queueIndex + 1) + ".");
                        burgerStock -= burgersRequired;
                    } else {
                        if (waitingListCount < WAITING_LIST_SIZE) {
                            waitingListRear = (waitingListRear + 1) % WAITING_LIST_SIZE;
                            waitingList[waitingListRear] = customer;
                            waitingListCount++;
                            System.out.println("Customer " + customer.getFirstName() + " " + customer.getLastName() + " added to the waiting list.");
                            burgerStock -= burgersRequired;
                        } else {
                            System.out.println("All queues and the waiting list are full. Customer " + customer.getFirstName() + " " + customer.getLastName() + " could not be added.");
                        }
                    }
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Error reading customer data from file: " + e.getMessage());
        }
    }


    private static void processNumericChoice(int choice) {
        // Placeholder implementation for processing numeric choice
        System.out.println("Processing numeric choice: " + choice);
    }

    private static void processStringChoice(String choice) {
        // Placeholder implementation for processing string choice
        System.out.println("Processing string choice: " + choice);
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
        System.out.println("100 or VFQ: View all customers in queues");
        System.out.println("101 or VEQ: View empty queues");
        System.out.println("102 or ACQ: Add customer to a queue");
        System.out.println("103 or RCQ: Remove customer from a queue");
        System.out.println("104 or PCQ: Remove a served customer");
        System.out.println("105 or VCS: View all customers sorted in alphabetic order");
        System.out.println("106 or SPD: Store program data");
        System.out.println("107 or LPD: Load program data");
        System.out.println("108 or STK: View remaining burger stock");
        System.out.println("109 or AFS: Add burgers to stock");
        System.out.println("110 or IFQ: View income");
        System.out.println("999 or EXT: Exit program");
        System.out.println("\nEnter your choice:");
    }

    private static void viewAllQueues() {
        for (int i = 0; i < MAX_CUSTOMERS_PER_QUEUE.length; i++) {
            System.out.println("\nQueue " + (i + 1) + ":");
            if (queues[i].isEmpty()) {
                System.out.println("No customers in the queue.");
            } else {
                for (Customer customer : queues[i]) {
                    System.out.println(customer);
                }
            }
        }
    }

    private static void viewEmptyQueues() {
        boolean foundEmptyQueue = false;
        for (int i = 0; i < MAX_CUSTOMERS_PER_QUEUE.length; i++) {
            if (queues[i].isEmpty()) {
                System.out.println("Queue " + (i + 1) + " is empty.");
                foundEmptyQueue = true;
            }
        }
        if (!foundEmptyQueue) {
            System.out.println("No empty queues found.");
        }
    }

    private static void addCustomerToQueue() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter customer's first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter customer's last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter the number of burgers required: ");
        int burgersRequired = scanner.nextInt();

        Customer customer = new Customer(firstName, lastName, burgersRequired);

        int queueIndex = findSmallestQueue();
        if (queueIndex != -1) {
            queues[queueIndex].add(customer);
            System.out.println("Customer " + customer.getFirstName() + " " + customer.getLastName() + " added to queue " + (queueIndex + 1) + ".");
            burgerStock -= burgersRequired; // Subtract from burger stock
        } else {
            if (waitingListCount < WAITING_LIST_SIZE) {
                waitingListRear = (waitingListRear + 1) % WAITING_LIST_SIZE;
                waitingList[waitingListRear] = customer;
                waitingListCount++;
                System.out.println("Customer " + customer.getFirstName() + " " + customer.getLastName() + " added to the waiting list.");
                burgerStock -= burgersRequired; // Subtract from burger stock
            } else {
                System.out.println("All queues and the waiting list are full. Customer could not be added.");
            }
        }
    }


    private static int findSmallestQueue() {
        int minSize = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < MAX_CUSTOMERS_PER_QUEUE.length; i++) {
            if (queues[i].size() < minSize && queues[i].size() < MAX_CUSTOMERS_PER_QUEUE[i]) {
                minSize = queues[i].size();
                minIndex = i;
            }
        }
        return minIndex;
    }

    private static void removeCustomerFromQueue(Scanner scanner) {
        System.out.print("Enter the queue number: ");
        int queueNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (queueNumber >= 1 && queueNumber <= MAX_CUSTOMERS_PER_QUEUE.length) {
            Queue<Customer> queue = queues[queueNumber - 1];

            if (queue.isEmpty()) {
                System.out.println("No customers in the queue.");
            } else {
                System.out.println("Customers in Queue " + queueNumber + ":");
                int count = 1;

                for (Customer customer : queue) {
                    System.out.println(count + ". " + customer.getFirstName() + " " + customer.getLastName());
                    count++;
                }

                System.out.print("Enter the customer number to remove: ");
                int customerNumber = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (customerNumber >= 1 && customerNumber <= queue.size()) {
                    Customer removedCustomer = null;
                    count = 1;
                    Iterator<Customer> iterator = queue.iterator();

                    while (iterator.hasNext()) {
                        Customer customer = iterator.next();
                        if (count == customerNumber) {
                            removedCustomer = customer;
                            iterator.remove();
                            break;
                        }
                        count++;
                    }

                    if (removedCustomer != null) {
                        System.out.println("Customer " + removedCustomer.getFirstName() + " " + removedCustomer.getLastName() +
                                " removed from queue " + queueNumber + ".");
                    } else {
                        System.out.println("Invalid customer number.");
                    }
                } else {
                    System.out.println("Invalid customer number.");
                }
            }
        } else {
            System.out.println("Invalid queue number.");
        }
    }




    private static void removeServedCustomer(Scanner scanner) {
        System.out.print("Enter the queue number: ");
        int queueNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (queueNumber >= 1 && queueNumber <= MAX_CUSTOMERS_PER_QUEUE.length) {
            Queue<Customer> queue = queues[queueNumber - 1];

            if (queue.isEmpty()) {
                System.out.println("No customers in the queue.");
            } else {
                System.out.println("Customers in Queue " + queueNumber + ":");
                int count = 1;

                for (Customer customer : queue) {
                    System.out.println(count + ". " + customer.getFirstName() + " " + customer.getLastName());
                    count++;
                }

                System.out.print("Enter the customer number to remove: ");
                int customerNumber = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (customerNumber >= 1 && customerNumber <= queue.size()) {
                    Customer removedCustomer = null;
                    count = 1;
                    Iterator<Customer> iterator = queue.iterator();
                    while (iterator.hasNext()) {
                        removedCustomer = iterator.next();
                        if (count == customerNumber) {
                            iterator.remove();
                            System.out.println("Customer " + removedCustomer.getFirstName() + " " + removedCustomer.getLastName() + " removed from queue " + queueNumber + ".");
                            break;
                        }
                        count++;
                    }

                    if (removedCustomer != null) {
                        burgerStock += removedCustomer.getBurgersRequired(); // Add to burger stock
                        System.out.println(removedCustomer.getBurgersRequired() + " burgers added back to the stock.");

                        // Check if there are customers in the waiting list
                        if (waitingListCount > 0) {
                            Customer nextCustomer = waitingList[waitingListFront];
                            waitingListFront = (waitingListFront + 1) % WAITING_LIST_SIZE;
                            waitingListCount--;

                            queues[queueNumber - 1].add(nextCustomer);
                            System.out.println("Customer " + nextCustomer.getFirstName() + " " + nextCustomer.getLastName() + " added to queue " + queueNumber + " from the waiting list.");
                        }
                    }
                } else {
                    System.out.println("Invalid customer number.");
                }
            }
        } else {
            System.out.println("Invalid queue number.");
        }
    }


    private static void viewCustomersSorted() {
        List<Customer> allCustomers = new ArrayList<>();
        for (int i = 0; i < MAX_CUSTOMERS_PER_QUEUE.length; i++) {
            allCustomers.addAll(queues[i]);
        }
        if (allCustomers.isEmpty()) {
            System.out.println("No customers in the queues.");
        } else {
            Collections.sort(allCustomers, Comparator.comparing(Customer::getFirstName, String.CASE_INSENSITIVE_ORDER));
            System.out.println("Customers sorted by first name:");
            for (Customer customer : allCustomers) {
                System.out.println(customer);
            }
        }
    }



    private static void storeProgramData() {
        try {
            FileWriter fileWriter = new FileWriter("programData.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Store queue details
            for (int i = 0; i < MAX_CUSTOMERS_PER_QUEUE.length; i++) {
                Queue<Customer> queue = queues[i];
                bufferedWriter.write("Queue " + (i + 1) + ":\n");

                if (queue.isEmpty()) {
                    bufferedWriter.write("No customers in the queue.\n");
                } else {
                    for (Customer customer : queue) {
                        bufferedWriter.write("Customer: " + customer.getFirstName() + " " + customer.getLastName() +
                                ", Burgers Required: " + customer.getBurgersRequired() + "\n");
                    }
                }

                bufferedWriter.write("\n");
            }

            // Store waiting list details
            bufferedWriter.write("Waiting List:\n");
            if (waitingListCount == 0) {
                bufferedWriter.write("No customers in the waiting list.\n");
            } else {
                for (int i = waitingListFront; i <= waitingListRear; i++) {
                    int index = i % WAITING_LIST_SIZE;
                    Customer customer = waitingList[index];
                    bufferedWriter.write("Customer: " + customer.getFirstName() + " " + customer.getLastName() +
                            ", Burgers Required: " + customer.getBurgersRequired() + "\n");
                }
            }

            bufferedWriter.close();
            fileWriter.close();
            System.out.println("Program data stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing program data: " + e.getMessage());
        }
    }

    private static void loadProgramData() {
        try {
            FileReader fileReader = new FileReader("programData.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

            bufferedReader.close();
            fileReader.close();
            System.out.println("Program data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading program data: " + e.getMessage());
        }
    }


    private static void viewRemainingBurgerStock() {
        if(burgerStock<=WARNING_STOCK){
            System.out.println("Remaining burger stock: " + burgerStock);
            System.out.println("WARNING: Burger stock is running low!");
        } else {
            System.out.println("Remaining burger stock: " + burgerStock);
        }
    }

    private static void addBurgersToStock(Scanner scanner) {
        System.out.print("Enter the number of burgers to add to stock: ");
        int burgersToAdd = scanner.nextInt();
        burgerStock += burgersToAdd;
        System.out.println(burgersToAdd + " burgers added to stock.");
        viewRemainingBurgerStock(); //Display the total burger stock
        return; //Exit the method
    }

    private static void viewIncome() {
        int totalIncome = (MAX_BURGERS - burgerStock) * BURGER_PRICE;
        System.out.println("Total income: $" + totalIncome);
    }
}