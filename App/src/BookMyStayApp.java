import java.util.HashMap;
import java.util.Map;

/**
 * UseCase3InventorySetup demonstrates centralized room inventory
 * management using a HashMap. This replaces scattered availability
 * variables from the previous use case.
 *
 * @author Student
 * @version 3.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Application Version: 3.1");
        System.out.println("-----------------------------------");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types with availability
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        // Display inventory
        System.out.println("Current Room Inventory:");
        inventory.displayInventory();

        // Example update
        System.out.println("\nUpdating inventory after a booking...");
        inventory.updateAvailability("Single Room", 4);

        System.out.println("\nUpdated Room Inventory:");
        inventory.displayInventory();

        System.out.println("-----------------------------------");
        System.out.println("Application terminated successfully.");
    }
}


/**
 * RoomInventory manages centralized room availability
 * using a HashMap structure.
 *
 * All room availability information is maintained
 * in one place to ensure consistency across the system.
 *
 * @author Student
 * @version 3.0
 */
class RoomInventory {

    // HashMap storing room type and available count
    private HashMap<String, Integer> inventory;

    /**
     * Constructor initializes the inventory map
     */
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    /**
     * Adds a new room type with its availability
     */
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    /**
     * Retrieves availability for a room type
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Updates availability for a specific room type
     */
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    /**
     * Displays the current inventory state
     */
    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available Rooms: " + entry.getValue());
        }
    }
}