import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task>, Repeatable {
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

    @Override
    public boolean cameOn(String dateStr) {
        LocalDate date = Planner.stringToDate(dateStr);
        return this.date.equals(date);
    }

     enum TaskType {
        PERSONAL("Личное"),
        WORKING("Рабочее");

        private final String taskType;

        TaskType(String taskType) {
            this.taskType = taskType;
        }
    }

    enum Repeatability {
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

    public Task(String title, String description, TaskType type, LocalDate date, LocalTime time, Repeatability repeatability) {
        this.id = count++;
        this.title = title;
        this.description = description;
        this.type = type;
        this.date = date;
        this.time = time;
        this.repeatability = repeatability;
    }

    public Task(String title) {
        this(title, "Описание отсутствует", 1, "01.01.2000", "00.00", 1);
    }

    // сеттеры

    public void setTitle(String title) {
        this.title = checkTitle(title);
    }

    public void setDescription(String description) {
        this.description = checkDescription(description);
    }

    public void setType(int type) {
        this.type = checkType(type);
    }

    public void setDate(String dateStr) {
        this.date = Planner.stringToDate(dateStr);
    }

    public void setTime(String time) {
        this.time = checkTime(time);
    }

    public void setRepeatability(int repeat) {
        this.repeatability = checkRepeatability(repeat);
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

    public Repeatability getRepeatability() {
        return repeatability;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    //методы

    public static String checkTitle(String title) {
        if (title == null || title.isBlank() || title.isEmpty()) {
            return "Дело №" + count;
        } else {
            return title;
        }
    }

    public static String checkDescription(String description) {
        if (description == null || description.isBlank() || description.isEmpty()) {
            return "Описание отсутствует";
        } else {
            return description;
        }
    }

    public static TaskType checkType(int type) {
        if (type > 0 && type <= TaskType.values().length) {
            return TaskType.values()[type - 1];
        } else {
            throw new RuntimeException();
        }
    }

    public static LocalTime checkTime(String time) {
        time = time.replaceAll("\\p{Punct}|\\s", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Hmm");
        return LocalTime.parse(time, formatter);
    }

    public static Repeatability checkRepeatability(int repeat) {
        if (repeat > 0 && repeat <= Repeatability.values().length) {
            return Repeatability.values()[repeat - 1];
        } else {
            throw new RuntimeException();
        }
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
                " " + this.getRepeatability().repeat;
    }
}
