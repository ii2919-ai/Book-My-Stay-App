import java.io.*;
import java.util.*;

class Booking implements Serializable {
    String user;
    String roomType;

    Booking(String user, String roomType) {
        this.user = user;
        this.roomType = roomType;
    }

    public String toString() {
        return user + " -> " + roomType;
    }
}

class Inventory implements Serializable {
    Map<String, Integer> rooms = new HashMap<>();

    void addRoom(String type, int count) {
        rooms.put(type, rooms.getOrDefault(type, 0) + count);
    }

    boolean bookRoom(String type) {
        if (rooms.getOrDefault(type, 0) > 0) {
            rooms.put(type, rooms.get(type) - 1);
            return true;
        }
        return false;
    }

    public String toString() {
        return rooms.toString();
    }
}

class SystemState implements Serializable {
    Inventory inventory;
    List<Booking> bookings;

    SystemState(Inventory inventory, List<Booking> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

class PersistenceService {
    private static final String FILE_NAME = "bookmystay.dat";

    static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    static SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (SystemState) ois.readObject();
        } catch (Exception e) {
            System.out.println("No previous data found or file corrupted. Starting fresh.");
            return null;
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Inventory inventory;
        List<Booking> bookings;

        SystemState loadedState = PersistenceService.load();

        if (loadedState != null) {
            inventory = loadedState.inventory;
            bookings = loadedState.bookings;
            System.out.println("System recovered successfully");
        } else {
            inventory = new Inventory();
            bookings = new ArrayList<>();
            inventory.addRoom("Deluxe", 5);
            inventory.addRoom("Suite", 3);
        }

        if (inventory.bookRoom("Deluxe")) {
            bookings.add(new Booking("User1", "Deluxe"));
        }

        if (inventory.bookRoom("Suite")) {
            bookings.add(new Booking("User2", "Suite"));
        }

        System.out.println("Current Inventory: " + inventory);
        System.out.println("Bookings: " + bookings);

        System.out.println("Saving system state...");
        PersistenceService.save(new SystemState(inventory, bookings));
        System.out.println("Shutdown complete");
    }
}