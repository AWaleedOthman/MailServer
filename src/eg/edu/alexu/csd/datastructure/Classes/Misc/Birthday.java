package eg.edu.alexu.csd.datastructure.Classes.Misc;

public class Birthday {
    int day, month, year;

    public void setBirthday(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getBirthday() {
        return day + "/" + month + "/" + year;
    }

    public Birthday(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
