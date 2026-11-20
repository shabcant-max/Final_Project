package recordshop.model;


public class Record {

    private String idnumber;
    private String title;
    private String artist;
    private double price;
    private boolean instock;
    private static int nextID = 1;

    public Record(double price, String artist, boolean instock, String title) {
        this(String.valueOf(nextID++), price, artist, instock, title);
    }

    public Record(String idnumber, double price, String artist, boolean instock, String title) {
        setIdnumber(idnumber);
        setPrice(price);
        setArtist(artist);
        setAvailability(instock);
        setTitle(title);
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        if (idnumber == null || idnumber.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        this.idnumber = idnumber.trim();
    }

    public static int getNextID() {
        return nextID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.title = title.trim().toUpperCase();
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        if (artist == null || artist.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist cannot be empty.");
        }
        this.artist = artist.trim().toUpperCase();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero: " + price);
        }
        this.price = price;
    }

    public boolean getAvailability() {
        return instock;
    }

    public void setAvailability(boolean instock) {
        this.instock = instock;
    }

    @Override
    public String toString() {
        return String.format("#%-3s %-28s %-24s $%7.2f  %-12s",
                idnumber,
                title,
                artist,
                price,
                instock ? "IN STOCK" : "OUT OF STOCK");
    }
}