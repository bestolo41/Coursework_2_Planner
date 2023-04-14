import java.time.LocalDate;
import java.time.LocalTime;

public class WeeklyTask extends Task {
    public WeeklyTask(String title, String description, int type, String date, String time, int repeat) {
        super(title, description, type, date, time, repeat);
    }

    public WeeklyTask(String title, String description, TaskType type, LocalDate date, LocalTime time, Repeatability repeatability) {
        super(title, description, type, date, time, repeatability);
    }

    @Override
    public boolean cameOn(String dateStr) {
        LocalDate date = Planner.stringToDate(dateStr);
        return this.getDate().equals(date) || (this.getDate().isBefore(date) && this.getDate().getDayOfWeek().equals(date.getDayOfWeek()));
    }
}
