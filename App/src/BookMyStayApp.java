import java.util.*;

// Reservation (Booking Intent)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO removal
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory Service (State + Synchronization)
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> " + availability.get(type));
        }
    }
}

// Booking Service (Core Allocation Logic)
class BookingService {
    private InventoryService inventory;

    // Map<RoomType, Set<RoomIDs>>
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Global set to ensure uniqueness across all rooms
    private Set<String> usedRoomIds = new HashSet<>();

    private int roomCounter = 1;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    // Generate Unique Room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + roomCounter++;
        } while (usedRoomIds.contains(roomId));

        usedRoomIds.add(roomId);
        return roomId;
    }

    // Process booking request (Atomic Logic)
    public void processReservation(Reservation r) {
        String type = r.getRoomType();

        System.out.println("\nProcessing request for " + r.getGuestName());

        // Step 1: Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking Failed: No rooms available for " + type);
            return;
        }

        // Step 2: Generate unique room ID
        String roomId = generateRoomId(type);

        // Step 3: Assign room (Map + Set)
        allocatedRooms.putIfAbsent(type, new HashSet<>());
        allocatedRooms.get(type).add(roomId);

        // Step 4: Update inventory immediately
        inventory.decrement(type);

        // Step 5: Confirm booking
        System.out.println("Booking Confirmed!");
        System.out.println("Guest: " + r.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Allocated Room ID: " + roomId);
    }

    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");

        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + " -> " + allocatedRooms.get(type));
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);

        // Step 2: Create Booking Queue (FIFO)
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Arun", "Single"));
        queue.addRequest(new Reservation("Priya", "Single"));
        queue.addRequest(new Reservation("Karan", "Single")); // Should fail
        queue.addRequest(new Reservation("Meena", "Double"));

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process Queue (FIFO Order)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processReservation(r);
        }

        // Step 5: Display Final State
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}