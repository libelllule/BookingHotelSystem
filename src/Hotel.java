import java.io.Serializable;
public class Hotel implements Serializable {
    private int id;
    private String name;
    private String city;
    private int rooms;
    private double price;
    public Hotel(int id, String name, String city, int rooms, double price){
        this.id =id;
        this.name = name;
        this.city = city;
        this.rooms = rooms;
        this.price = price;
    }
    @Override
    public String toString(){
        return "Hotel [id=" + id + ", name=" + name + ", city=" + city + ", rooms=" + rooms + ", price=" + price + "]";
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Hotel hotel = (Hotel) obj;
        return id == hotel.id;
    }
    @Override
    public int hashCode(){
        return id;
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
    public int getRooms() {
        return rooms;
    }
    public double getPrice() {
        return price;
    }
    public void setRooms(int rooms) {
        this.rooms = rooms;
    }
}