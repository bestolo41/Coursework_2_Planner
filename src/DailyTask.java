import java.time.LocalDate;
import java.time.LocalTime;

public class DailyTask extends Task {
    public DailyTask(String title, String description, int type, String date, String time, int repeat) {
        super(title, description, type, date, time, repeat);
    }

    public DailyTask(String title, String description, TaskType type, LocalDate date, LocalTime time, Repeatability repeatability) {
        super(title, description, type, date, time, repeatability);
    }

    @Override
    public boolean cameOn(String dateStr) {
        LocalDate date = Planner.stringToDate(dateStr);
        return this.getDate().isBefore(date) || this.getDate().equals(date);
    }
}
