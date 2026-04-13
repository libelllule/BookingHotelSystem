import java.io.Serializable;
public class Hotel implements Serializable {
    private int id;
    private String name;
    private String city;
    private int roomsTotal;
    private int availableRooms;
    private double price;

    public Hotel(int id, String name, String city, int roomsTotal, double price) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.roomsTotal = roomsTotal;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getRoomsTotal() {
        return roomsTotal;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    @Override
    public String toString() {
        return "Hotel [id=" + id + ", name=" + name + ", city=" + city + ", totalRooms=" + roomsTotal + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Hotel hotel = (Hotel) obj;
        return id == hotel.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}