import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.time.LocalDate.now;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                printMenu();
                System.out.print("Выберите пункт меню: ");
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    scanner.nextLine();
                    switch (menu) {
                        case 1:
                            inputTask(scanner);
                            break;
                        case 2:
                            deleteTask(scanner);
                            break;
                        case 3:
                            printTasks();
                            break;
                        case 4:
                            printTasks(scanner);
                            break;
                        case 5:
                            Planner.printDeletedTasks();
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.println("Выберите пункт меню из списка!");
                }
            }
        }
    }

    private static void inputTask(Scanner scanner) {
        Task task = new Task(null);

        System.out.println("\n===========================================================================");
        System.out.print("Введите название задачи: ");
        task.setTitle(scanner.nextLine());

        System.out.println("\n===========================================================================");
        System.out.print("Введите описание задачи (не обязательно): ");
        task.setDescription(scanner.nextLine());

        System.out.println("\n===========================================================================");
        while (true) {
            System.out.print("Введите дату выполнения: ");
            try {
                task.setDate(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println(ANSI_RED + "НЕКОРРЕКТНЫЙ ФОРМАТ ДАТЫ" + ANSI_RESET);
            }
        }

        System.out.println("\n===========================================================================");
        while (true) {
            System.out.print("Введите время выполнения: ");
            try {
                task.setTime(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println(ANSI_RED + "НЕКОРРЕКТНЫЙ ФОРМАТ ВРЕМЕНИ" + ANSI_RESET);
            }
        }

        System.out.println("\n===========================================================================");
        System.out.println(
                """
                        1. Личная
                        2. Рабочая
                        """
        );
        while (true) {
            System.out.print("Выберите тип задачи: ");
            int taskType = scanner.nextInt();
            try {
                task.setType(taskType);
                break;
            } catch (Exception e) {
                System.out.println(ANSI_RED + "ВЫБЕРИТЕ ПУНКТ МЕНЮ" + ANSI_RESET);
            }
        }

        System.out.println("\n===========================================================================");
        System.out.println(
                """
                        1. Однократно
                        2. Ежедневно
                        3. Еженедельно
                        4. Ежемесячно
                        5. Ежегодно
                        """
        );
        while (true) {
            System.out.print("Выберите повторяемость задачи: ");
            int taskRepeat = scanner.nextInt();
            scanner.nextLine();
            try {
                task.setRepeatability(taskRepeat);
                break;
            } catch (Exception e) {
                System.out.println(ANSI_RED + "ВЫБЕРИТЕ ПУНКТ МЕНЮ" + ANSI_RESET);
            }
        }

        Planner.addTask(task);
    }

    private static void printMenu() {
        System.out.println( "\n===========================================================================");
        System.out.println(
                """
                        1. Добавить задачу
                        2. Удалить задачу
                        3. Получить задачи на сегодня
                        4. Получить задачи на указанный день
                        5. Получить список удаленных задач
                        0. Выход
                        """

        );


    }

    private static void deleteTask(Scanner scanner) {
        System.out.println( "\n===========================================================================");
        System.out.println("Введите ID задачи: ");
        int answer = scanner.nextInt();
        Planner.removeTask(answer);
        System.out.println("Задача " + Planner.getTasks().get(answer).getTitle() + " удалена");
    }

    private static void printTasks(Scanner scanner) {
        System.out.println( "\n===========================================================================");
        System.out.println("Укажите дату: ");
        String answer = scanner.next();
        try {
            Planner.printTasksForDay(answer);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "НЕКОРРЕКТНЫЙ ФОРМАТ ДАТЫ" + ANSI_RESET);
            printTasks(scanner);
        }
    }

    private static void printTasks() {
        System.out.println( "\n===========================================================================");
        String dateStr = DateTimeFormatter.ofPattern("ddMMyyyy").format(now());
        try {
            Planner.printTasksForDay(dateStr);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "НЕКОРРЕКТНЫЙ ФОРМАТ ДАТЫ" + ANSI_RESET);
            printTasks();
        }
    }


}