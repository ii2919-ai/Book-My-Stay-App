import java.util.*;

class Booking {
    String bookingId;
    String roomType;
    String roomId;
    boolean active;

    Booking(String bookingId, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.active = true;
    }
}

class CancellationService {
    Map<String, Booking> bookings;
    Map<String, Integer> inventory;
    Stack<String> rollbackStack;

    CancellationService(Map<String, Booking> bookings, Map<String, Integer> inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
        this.rollbackStack = new Stack<>();
    }

    void cancelBooking(String bookingId) {
        if (!bookings.containsKey(bookingId)) {
            System.out.println("Invalid booking ID");
            return;
        }

        Booking booking = bookings.get(bookingId);

        if (!booking.active) {
            System.out.println("Booking already cancelled");
            return;
        }

        rollbackStack.push(booking.roomId);

        inventory.put(booking.roomType, inventory.getOrDefault(booking.roomType, 0) + 1);

        booking.active = false;

        System.out.println("Booking cancelled successfully: " + bookingId);
    }

    void showRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);

        Map<String, Booking> bookings = new HashMap<>();

        Booking b1 = new Booking("B101", "Single", "R1");
        Booking b2 = new Booking("B102", "Double", "R2");

        bookings.put(b1.bookingId, b1);
        bookings.put(b2.bookingId, b2);

        inventory.put("Single", inventory.get("Single") - 1);
        inventory.put("Double", inventory.get("Double") - 1);

        CancellationService service = new CancellationService(bookings, inventory);

        service.cancelBooking("B101");
        service.cancelBooking("B101");
        service.cancelBooking("B999");

        service.showRollbackStack();

        System.out.println("Inventory: " + inventory);
    }
}