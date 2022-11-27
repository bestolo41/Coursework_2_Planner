import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private static int count = 0;
    private String title;
    private String description;
    private TaskType type;
    private LocalDate date;
    private LocalTime time;
    private final int id;
    private Repeatability repeatability;
    private boolean isDeleted = false;

    @Override
    public int compareTo(Task o) {
        return 0;
    }

    private enum TaskType {
        PERSONAL("Личное"),
        WORKING("Рабочее");

        private final String taskType;

        TaskType(String taskType) {
            this.taskType = taskType;
        }
    }

    private enum Repeatability {
        ONE_TIME("Однократно"),
        DAILY("Ежедневно"),
        WEEKLY("Еженедельно"),
        MONTHLY("Ежемесячно"),
        YEARLY("Ежегодно");

        private final String repeat;

        Repeatability(String repeatability) {
            this.repeat = repeatability;
        }
    }


//    конструкторы

    
    public Task(String title, String description, int type, String date, String time, int repeat) {
        this.id = count++;
        setTitle(title);
        setDescription(description);
        setType(type);
        setDate(date);
        setTime(time);
        setRepeatability(repeat);
    }

    public Task(String title) {
        this(title, "Описание отсутствует", 1, "01.01.2000", "00.00", 1);
    }

    // сеттеры

    public void setTitle(String title) {
        if (title == null || title.isBlank() || title.isEmpty()) {
            this.title = "Дело №" + getId();
        } else {
            this.title = title;
        }
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank() || description.isEmpty()) {
            this.description = "Описание отсутствует";
        } else {
            this.description = description;
        }
    }

    public void setType(int type) {
        if (type > 0 && type <= TaskType.values().length) {
            this.type = TaskType.values()[type - 1];
        } else {
            throw new RuntimeException();
        }
    }

    public void setDate(String dateStr) {
        this.date = Planner.stringToDate(dateStr);
    }

    public void setTime(String time) {
        time = time.replaceAll("\\p{Punct}|\\s", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Hmm");
        this.time = LocalTime.parse(time, formatter);
    }

    public void setRepeatability(int repeat) {
        if (repeat > 0 && repeat <= Repeatability.values().length) {
            this.repeatability = Repeatability.values()[repeat - 1];
        } else {
            throw new RuntimeException();
        }
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    // геттеры

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type.taskType;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public String getRepeatability() {
        return repeatability.repeat;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    //методы

    public boolean cameOn(String dateStr) {
        LocalDate date = Planner.stringToDate(dateStr);
        return switch (this.repeatability) {
            case ONE_TIME -> this.date.equals(date);
            case DAILY -> this.date.isBefore(date) || this.date.equals(date);
            case WEEKLY ->
                    this.date.equals(date) || (this.date.isBefore(date) && this.date.getDayOfWeek().equals(date.getDayOfWeek()));
            case MONTHLY ->
                    this.date.equals(date) || (this.date.isBefore(date) && this.date.getDayOfMonth() == date.getDayOfMonth());
            case YEARLY ->
                    this.date.equals(date) || (this.date.isBefore(date) && this.date.getMonth().equals(date.getMonth()));
        };
    }

    //переопределение

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, type, id);
    }

    @Override
    public String toString() {
        return Planner.spaces(Planner.StrLength.ID.getValue(), Integer.toString(this.getId())) + "|" +                              //подбор ширины столбца
                " " + this.getTime() + " |" +
                Planner.spaces(Planner.StrLength.TITLE.getValue(), this.getTitle()) + "|" +
                Planner.spaces(Planner.StrLength.DESCRIPTION.getValue(), this.getDescription()) + "|" +
                Planner.spaces(Planner.StrLength.TYPE.getValue(), this.getType()) + "|" +
                " " + this.getRepeatability();
    }
}
