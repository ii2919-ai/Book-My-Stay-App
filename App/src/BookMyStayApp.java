import java.util.*;

// Domain मॉडल: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

// Inventory: State Holder (READ-ONLY access for search)
class Inventory {
    private Map<String, Integer> availabilityMap = new HashMap<>();

    public void addRoom(String type, int count) {
        availabilityMap.put(type, count);
    }

    public int getAvailability(String type) {
        return availabilityMap.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        // Defensive copy to prevent modification
        return new HashMap<>(availabilityMap);
    }
}

// Search Service: Handles read-only logic
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:\n");

        Map<String, Integer> availabilityData = inventory.getAllAvailability();

        boolean found = false;

        for (String roomType : availabilityData.keySet()) {
            int count = availabilityData.get(roomType);

            // Validation: Only show available rooms
            if (count > 0 && roomCatalog.containsKey(roomType)) {
                Room room = roomCatalog.get(roomType);

                room.displayDetails();
                System.out.println("Available Units: " + count);
                System.out.println("===========================");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Create Room Catalog (Domain Layer)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 2000,
                        Arrays.asList("WiFi", "TV")));

        roomCatalog.put("Double",
                new Room("Double", 3500,
                        Arrays.asList("WiFi", "TV", "AC")));

        roomCatalog.put("Suite",
                new Room("Suite", 6000,
                        Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        // Step 2: Setup Inventory (State Holder)
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0);  // Should NOT appear
        inventory.addRoom("Suite", 2);

        // Step 3: Create Search Service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Step 4: Guest initiates search (READ-ONLY)
        searchService.searchAvailableRooms();
    }
}