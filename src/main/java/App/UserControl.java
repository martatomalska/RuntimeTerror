package App;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import Reader.DataReader;

import Model.*;
import Reader.ScanErrorsHolder;
import Report.Report;
import Report.Report1Builder;
import Report.Report2Builder;
import Report.Report3Builder;
import Report.Report4Builder;
import Report.Report5Builder;
import Report.ReportBuilder;
import Report.ReportPrinter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class UserControl {

    private Scanner sc = new Scanner(System.in);
    private String userOption;
    private String path;
    private Model model;
    private ReportBuilder reportBuilder;
    private Report report;
    private DataReader dataReader;


    public UserControl(String path) throws IOException, InvalidFormatException {
        this.path = path;
        model = new Model(path);

    }

    public void controlLoop() throws IOException, InvalidFormatException {
        appHeaders();
        do {
            showOption();
            String userOption = inputUserOption();
            switch (userOption) {
                case "1":
                    generateReport1();
                    break;
                case "2":
                    generateReport2();
                    break;
                case "3":
                    generateReport3();
                    break;
                case "4":
                    generateReport4();
                    break;
                case "5":
                    generateReport5();
                    break;
                case "9":
                    generateErrorsLog();
                    break;
                case "0":
                    exit();
                    break;
                default:
                    System.err.println("Nie znam takiej opcji");
            }
        } while (!userOption.equals("0"));

    }
 

    public void showOption() {
        System.out.println("WYBIERZ OPCJE:");
        System.out.println("1. Generuj raport listy pracowników za podany rok: ");
        System.out.println("2. Generuj raport listy projektów za podany rok ");
        System.out.println("3. Szczegółowy wykaz pracy danego pracownika za podany rok");
        System.out.println("4. Procentowy udział danego pracownika w projekt za dany rok");
        System.out.println("5. Szczegółowy wykaz pracy pracowników w danym projekcie");
        System.out.println("9. Pokaż logi z odczytu pliku");
        System.out.println("0. Zakończ pracę z programem");
    }


    public String inputUserOption() {
        System.out.println("\n______________________");
        System.out.println("Wprowadź wybraną opcję\n");
        userOption = sc.nextLine();
        return userOption;
    }

    private void exit() {
        System.out.println("Koniec programu");
        sc.close();
    }

    private void appHeaders(){
        System.out.println("______                _____  _                    _____                                \n" +
                "| ___ \\              |_   _|(_)                  |_   _|                               \n" +
                "| |_/ / _   _  _ __    | |   _  _ __ ___    ___    | |    ___  _ __  _ __   ___   _ __ \n" +
                "|    / | | | || '_ \\   | |  | || '_ ` _ \\  / _ \\   | |   / _ \\| '__|| '__| / _ \\ | '__|\n" +
                "| |\\ \\ | |_| || | | |  | |  | || | | | | ||  __/   | |  |  __/| |   | |   | (_) || |   \n" +
                "\\_| \\_| \\__,_||_| |_|  \\_/  |_||_| |_| |_| \\___|   \\_/   \\___||_|   |_|    \\___/ |_|   \n" +
                "                                                                                       \nversion 1.0.0");
        System.out.println("----------------------------");
        System.out.println("");
    }

    private void generateReport4() throws InvalidFormatException, IOException{
        System.out.println();
        System.out.println("Podaj za jaki rok mam wygenerować raport");
        int reportYear = sc.nextInt();
        sc.nextLine();
        reportBuilder= new Report4Builder(reportYear);
        report = reportBuilder.buildReport(model);
        ReportPrinter.printReport(report);
        System.out.println();
    }

    private void generateReport5() throws InvalidFormatException, IOException{
        System.out.println();
        System.out.println("Podaj nazwę projektu");
        String projectName = sc.nextLine();
        reportBuilder= new Report5Builder(projectName);
        report = reportBuilder.buildReport(model);
        ReportPrinter.printReport(report);
        System.out.println();
    }

    private void generateReport1() throws InvalidFormatException, IOException{
        dateRangeGenerator();
        int reportYear;
        System.out.println("Podaj za jaki rok mam wygenerować raport");
        try {
            reportYear = sc.nextInt();
            sc.nextLine();
            reportBuilder= new Report1Builder(reportYear);
            report = reportBuilder.buildReport(model);
            ReportPrinter.printReport(report);
        System.out.println("\nCzy chcesz zapsiać raport do pliku Y/N -- przykładowa fk.\n");
        } catch (InputMismatchException e){
            System.err.println("Wprowadziłeś błędne dane");
        }
    }

    private void dateRangeGenerator() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        List<String> yearProject = new ArrayList<>();
        List<Employee> employeeList = model.getEmployeeList();
        for (Employee employee : employeeList) {
            List<Task> taskList = employee.getTaskList();
            for (Task task : taskList) {
                Date allDates = task.getTaskDate();
                String year = simpleDateFormat.format(allDates);
                if (!yearProject.contains(year)){
                    yearProject.add(year);
                }
            }
        }
        Collections.sort(yearProject);
        System.out.println("\nRaporty są dostępne za lata: " + yearProject +"\n");
    }


    private void generateReport2(){
        dateRangeGenerator();
        int reportYear;
        try {
            System.out.println("Podaj za jaki rok mam wygenerować raport");
            reportYear = sc.nextInt();
            sc.nextLine();
            System.out.println();
            reportBuilder = new Report2Builder(reportYear);
            report = reportBuilder.buildReport(model);
            ReportPrinter.printReport(report);
            System.out.println();
        } catch (InputMismatchException e) {
            System.err.println("Wprowadziłeś błędne dane");
        }

    }

    private void generateReport3(){
        System.out.println("Podaj imię i nazwisko pracownika");
        String name = sc.nextLine();
        System.out.println("Podaj za jaki rok mam wygenerować raport");
        int reportYear = sc.nextInt();
        sc.nextLine();
        System.out.println();
        reportBuilder = new Report3Builder(reportYear,name);
        report = reportBuilder.buildReport(model);
        ReportPrinter.printReport(report);
        System.out.println();
    }
    private void generateErrorsLog(){
        ScanErrorsHolder.printScanErrors();
        System.out.println();
    }
}
