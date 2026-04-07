import java.util.*;

class BookingRequest {
    String guestName;
    String roomType;

    BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingSystem {
    private Map<String, Integer> inventory = new HashMap<>();
    private Queue<BookingRequest> requestQueue = new LinkedList<>();

    BookingSystem() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }

    public synchronized void addRequest(BookingRequest request) {
        requestQueue.add(request);
        notifyAll();
    }

    public synchronized BookingRequest getRequest() {
        while (requestQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return requestQueue.poll();
    }

    public void processBooking() {
        BookingRequest request = getRequest();
        synchronized (this) {
            int available = inventory.getOrDefault(request.roomType, 0);
            if (available > 0) {
                inventory.put(request.roomType, available - 1);
                System.out.println(Thread.currentThread().getName() +
                        " booked " + request.roomType + " room for " + request.guestName);
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " failed booking for " + request.guestName + " (No rooms available)");
            }
        }
    }

    public void printInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

class BookingWorker extends Thread {
    BookingSystem system;

    BookingWorker(BookingSystem system, String name) {
        super(name);
        this.system = system;
    }

    public void run() {
        for (int i = 0; i < 2; i++) {
            system.processBooking();
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingSystem system = new BookingSystem();

        system.addRequest(new BookingRequest("Guest1", "Single"));
        system.addRequest(new BookingRequest("Guest2", "Single"));
        system.addRequest(new BookingRequest("Guest3", "Single"));
        system.addRequest(new BookingRequest("Guest4", "Double"));
        system.addRequest(new BookingRequest("Guest5", "Double"));

        BookingWorker t1 = new BookingWorker(system, "Thread-1");
        BookingWorker t2 = new BookingWorker(system, "Thread-2");
        BookingWorker t3 = new BookingWorker(system, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        system.printInventory();
    }
}