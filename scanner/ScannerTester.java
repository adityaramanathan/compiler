package scanner;
import java.io.*;

/**
 * Tests that the Scanner scans the higher level language token by token and groups characters
 * correctly based on type of token. Throws an exception if the token is invalid but
 * continues scanning through the higher level language.
 * Uses the ScannerTest.txt and scannerTestAdvanced.txt files to test the scanner.
 * @author Aditya Ramanathan
 * @version 1/26/24
 */
public class ScannerTester
{
    /**
     * Main Tester Method for the Scanner class.
     * @param str - the string arguments
     * @throws ScanErrorException
     * @throws FileNotFoundException
     */
    public static void main(String[] str) throws ScanErrorException, FileNotFoundException
    {
        FileInputStream reader1 = new FileInputStream(new File("scannerTest.txt"));
        Scanner scanner = new Scanner(reader1);
        System.out.println("Scanner Test Results:");
        while(scanner.hasNext())
        {
            System.out.println(scanner.nextToken());
        }

        FileInputStream reader2 = new FileInputStream(new File("scannerTestAdvanced.txt"));
        scanner = new Scanner(reader2);
        System.out.println("\n" + "\n" + "Scanner Advanced Test Results:");
        while(scanner.hasNext())
        {
            System.out.println(scanner.nextToken());
        }
    }
}
