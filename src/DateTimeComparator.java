import java.util.Comparator;

public class DateTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getDate().isBefore(o2.getDate())) {
            return -1;
        } else if (o1.getDate().isAfter(o2.getDate())) {
            return 1;
        } else if (o1.getTime().isBefore(o2.getTime())) {
            return -1;
        } else if (o1.getTime().isAfter(o2.getTime())) {
            return 1;
        } else return 0;
    }
}
