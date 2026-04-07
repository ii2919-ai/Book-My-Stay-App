import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation (Booking Input)
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

// Inventory Service (State Protection)
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) throws InvalidBookingException {
        int current = getAvailability(type);

        if (current <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }

        availability.put(type, current - 1);
    }
}

// Validator (Fail-Fast Design)
class BookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type existence
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected: " + reservation.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Selected room type is not available.");
        }
    }
}

// Booking Service
class BookingService {
    private InventoryService inventory;
    private int roomCounter = 1;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {
        try {
            // Step 1: Validate (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate room (only if valid)
            String roomId = reservation.getRoomType().substring(0, 2).toUpperCase() + roomCounter++;

            inventory.decrement(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("\nBooking Confirmed!");
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Type: " + reservation.getRoomType());
            System.out.println("Room ID: " + roomId);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("\nBooking Failed: " + e.getMessage());
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Double", 0);

        // Step 2: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 3: Test Cases (Valid + Invalid)

        // Valid booking
        Reservation r1 = new Reservation("Arun", "Single");

        // Invalid: empty name
        Reservation r2 = new Reservation("", "Single");

        // Invalid: wrong room type
        Reservation r3 = new Reservation("Priya", "Suite");

        // Invalid: no availability
        Reservation r4 = new Reservation("Karan", "Double");

        // Process all bookings
        bookingService.processBooking(r1);
        bookingService.processBooking(r2);
        bookingService.processBooking(r3);
        bookingService.processBooking(r4);

        // System continues running safely
        System.out.println("\nSystem is stable and running after handling errors.");
    }
}