import java.util.*;

// Reservation: Represents booking intent
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all requests (READ-ONLY)
    public void displayQueue() {
        if (queue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Requests (FIFO Order):\n");

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (without removing)
    public Reservation peekNext() {
        return queue.peek();
    }

    // Get queue size
    public int getSize() {
        return queue.size();
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Create Booking Queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Step 2: Guests submit booking requests
        Reservation r1 = new Reservation("Arun", "Single");
        Reservation r2 = new Reservation("Priya", "Suite");
        Reservation r3 = new Reservation("Karan", "Double");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Step 3: Display queue (preserves arrival order)
        bookingQueue.displayQueue();

        // Step 4: Show next request (without processing)
        System.out.println("\nNext request to be processed:");
        Reservation next = bookingQueue.peekNext();

        if (next != null) {
            next.display();
        }

        // Step 5: Show total requests
        System.out.println("\nTotal Requests in Queue: " + bookingQueue.getSize());

        // IMPORTANT:
        // No inventory updates or booking allocation happens here.
        // This stage ONLY collects and orders requests.
    }
}