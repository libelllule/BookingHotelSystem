import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {
    private int id;
    private String guestName;
    private int hotelId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalPrice;

    public Booking(int id, String guestName, int hotelId,
                   LocalDate checkIn, LocalDate checkOut, double totalPrice) {
        this.id = id;
        this.guestName = guestName;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
    }
    @Override
    public String toString() {
        return "Booking [id=" + id + ", guest=" + guestName + ", hotelId=" + hotelId +
                ", checkIn=" + checkIn + ", checkOut=" + checkOut + ", total=" + totalPrice + "]";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Booking booking = (Booking) obj;
        return id == booking.id;
    }
    @Override
    public int hashCode() {
        return id;
    }
    public int getId() {
        return id;
    }
    public String getGuestName() {
        return guestName;
    }
    public int getHotelId() {
        return hotelId;
    }
    public LocalDate getCheckIn() {
        return checkIn;
    }
    public LocalDate getCheckOut() {
        return checkOut;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}
