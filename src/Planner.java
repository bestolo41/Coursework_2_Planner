import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class Planner {
    private static final Map<Integer, Task> tasks = new HashMap<>();

    public static Map<Integer, Task> getTasks() {
        return tasks;
    }

    //вспомогательное перечисление для ровных столбцов при печати списка задач
    enum StrLength {
        ID("ID"),
        TITLE("Название"),
        DESCRIPTION("Описание"),
        TYPE("Тип");
        private int value;
        private final String name;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        StrLength(String name) {
            this.name = name;
        }
    }


    //МЕТОДЫ

    //добавляет задачу в мап
    public static void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    //помечает задачу как удаленная
    public static void removeTask(int id) {
        tasks.get(id).setDeleted(true);
    }

    //печатает задачу на указанный день
    public static void printTasksForDay(String dateStr) {
        maxStringLength();
        System.out.println("\n" + Planner.stringToDate(dateStr).format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")));
        printTableTitle();
        int count = 0;
        for (Task task : sortedTasks()) {
            if (task.cameOn(dateStr) && !task.isDeleted()) {
                System.out.println(task);
                count++;
            }
        }
        if (count == 0) System.out.println("На эту дату задач нет");
    }

    //печатает список удаленных задач
    public static void printDeletedTasks() {
        maxStringLength();
        System.out.println( "\n===========================================================================");
        System.out.println("\nУдаленные задачи");
        printTableTitle();
        int count = 0;
        for (Task task : tasks.values()) {
            if (task.isDeleted()) {
                System.out.println(task);
                count++;
            }
        }
        if (count == 0) System.out.println("Удаленных задач нет");
    }


    //ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    //переводит дату из строкового вида в LocalDate
    public static LocalDate stringToDate(String dateStr) {
        dateStr = dateStr.replaceAll("\\p{Punct}|\\s", "");
        DateTimeFormatter formatter = switch (dateStr.length()) {
            case 8 -> DateTimeFormatter.ofPattern("ddMMyyyy");
            case 7 -> DateTimeFormatter.ofPattern("dMMyyyy");
            case 6 -> DateTimeFormatter.ofPattern("ddMMyy");
            case 5 -> DateTimeFormatter.ofPattern("dMMyy");
            default -> throw new RuntimeException("Некорректный формат даты");
        };
        return LocalDate.parse(dateStr, formatter);
    }

    //вычисляет наибольшую ширину каждого столбца и сохраняет в enum, чтоб использовать при печати
    private static void maxStringLength() {
        int maxIdLength = "ID".length();
        int maxTitleLength = "Название".length();
        int maxDescriptionLength = "Описание".length();
        int maxTypeLength = "Тип".length();

        for (Task task : tasks.values()) {
            if (Integer.toString(task.getId()).length() > maxIdLength) maxIdLength = Integer.toString(task.getId()).length();
            if (task.getTitle().length() > maxTitleLength) maxTitleLength = task.getTitle().length();
            if (task.getDescription().length() > maxDescriptionLength) maxDescriptionLength = task.getDescription().length();
            if (task.getType().length() > maxTypeLength) maxTypeLength = task.getType().length();
        }

        StrLength.ID.setValue(maxIdLength);
        StrLength.TITLE.setValue(maxTitleLength);
        StrLength.DESCRIPTION.setValue(maxDescriptionLength);
        StrLength.TYPE.setValue(maxTypeLength);
    }

    //вычисляет необходимое количество добавляемых пробелов, чтоб столбцы были одинаковой ширины
    static String spaces(int strLength, String text) {
        int left = (strLength - text.length())/2;
        int right = strLength - left - text.length();

        return " ".repeat(Math.max(0, left)) +
                text +
                " ".repeat(Math.max(0, right));
    }


    //преобразует мап в список и сортирует его по времени, чтоб задачи печатались по времени
    private static ArrayList<Task> sortedTasks() {
        ArrayList<Task> sortedTask = new ArrayList<>(tasks.size());
        sortedTask.addAll(tasks.values());
        Comparator<Task> DateTimeComparator = new DateTimeComparator();
        sortedTask.sort(DateTimeComparator);
        return sortedTask;
    }

    //печатает заголовок таблицы
    private static void printTableTitle() {
        System.out.println(
                spaces(StrLength.ID.value, StrLength.ID.name) + "|" +                                                   //подбор ширины столбца
                        " Время |" +
                        spaces(StrLength.TITLE.value, StrLength.TITLE.name) + "|" +
                        spaces(StrLength.DESCRIPTION.value, StrLength.DESCRIPTION.name) + "|" +
                        spaces(StrLength.TYPE.value, StrLength.TYPE.name) + "|" +
                        "Повторяемость");
    }
}


