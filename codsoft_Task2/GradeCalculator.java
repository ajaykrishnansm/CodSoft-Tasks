import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

class StudentMarksCard {
    private String studentName;
    private HashMap<String, Double> subjectMarks = new HashMap<>();
    private Double totalMarks = null;
    private Double totalPercentage = null;
    private Double averageMarks = null;
    private Double averagePercentage = null;
    private String grade = null;

    public StudentMarksCard(String name) {
        studentName = name;
    }

    public void setSubjectMarks() {
        try (Scanner keypad = new Scanner(System.in)) {
            boolean finished = false;
            while (!finished) {
                System.out.println("Enter the Subject Name:");
                String subject = keypad.nextLine();
                System.out.println("Enter the Mark:");
                try {
                    Double mark = keypad.nextDouble();
                    if (mark < 0 || mark > 100) {
                        System.out.println("Invalid mark. Please enter a value between 0 and 100.");
                    } else {
                        subjectMarks.put(subject, mark);
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    keypad.nextLine();
                }

                System.out.println("Do you want to add another subject? (Y/N)");
                String ch = keypad.next();
                keypad.nextLine();
                switch (ch.toLowerCase()) {
                    case "n" : 
                        finished = true;
                        break;
                    case "y" : 
                        continue;
                    default:
                        System.out.println("Invalid choice. Continuing...");
                }
            }
        }
    }

    private String setGrade(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    public StringBuffer getAllSubjectMarks() {
        if (!subjectMarks.isEmpty()) {
            StringBuffer enteredMarks = new StringBuffer();
            int count=0;
            for (HashMap.Entry<String, Double> entry : subjectMarks.entrySet()) {
                enteredMarks.append(++count+". ").append(entry.getKey()).append(", Marks: ").append(entry.getValue()).append(" out of "+100).append("\n");
            }
            return enteredMarks;
        }
        return null;
    }

    public void calculateMarks() {
        if (!subjectMarks.isEmpty()) {
            totalMarks = 0.00;
            for (HashMap.Entry<String, Double> entry : subjectMarks.entrySet()) {
                totalMarks += entry.getValue();
            }
            totalPercentage = (totalMarks / (100 * subjectMarks.size())) * 100;
            averageMarks = totalMarks / subjectMarks.size();
            averagePercentage = totalPercentage; 
            grade = setGrade(totalPercentage);
        } else {
            System.out.println("No Marks Entered!");
        }
    }

    public void getReport() {
        System.out.println("Name: " + studentName);
        StringBuffer subjectMarksReport = getAllSubjectMarks();
        if (subjectMarksReport != null) {
            System.out.println("Subjects and Marks:\n------------------------------------------\n" + subjectMarksReport.toString());
        } else {
            System.out.println("No Marks Entered");
        }
        System.out.println("Total Marks Scored: " + totalMarks);
        System.out.println("Maximium marks:"+100*subjectMarks.size());
        System.out.println("Total Percentage: " + totalPercentage);
        System.out.println("Average Marks: " + averageMarks);
        System.out.println("Average Percentage: " + averagePercentage);
        System.out.println("Grade: " + grade);
    }
}

public class GradeCalculator {
    public static void main(String[] args) {
        StudentMarksCard card1 = new StudentMarksCard("Aaron");
        card1.setSubjectMarks();
        card1.calculateMarks();
        card1.getReport();
    }
}
