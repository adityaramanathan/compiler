package parser;
import scanner.*;
import environment.*;
import ast.*;

import java.io.*;

/**
 * Tests that the Parser correctly parses through the stream of tokens.
 * This program first tests the interpreter using the environment provided
 * and the correct output is printed to the terminal. Then, it produces
 * asm files containing the MIPS code for the two test files. If these MIPS
 * files are run in MARS, the correct output should be printed there too.
 * @author Aditya Ramanathan
 * @version 6/2/24
 */
public class ParserTester
{
    /**
     * Main Tester Method for the Interpreter and the Compiler.
     * @param str the string arguments.
     * @precondition There needs to be files called "ParserTest.txt" and
     * "ParserTestAdvanced.txt". In addition, all variables in the global
     * part of the program must be initialized at the top of the pascal
     * code file.
     * @throws ScanErrorException if the token does not match when eat(tok) is called.
     * @throws FileNotFoundException if the input file does not exist in the folder.
     */
    public static void main(String[] str) throws ScanErrorException, FileNotFoundException
    {
        // Tests "ParserTest.txt" as an Interpreter
        FileInputStream reader = new FileInputStream(new File("ParserTest.txt"));
        Scanner sc = new Scanner(reader);
        Parser parser = new Parser(sc);
        Environment env = new Environment(null);
        parser.parseProgram().exec(env);

        // Tests "ParserTest.txt" as a Compiler
        reader = new FileInputStream(new File("ParserTest.txt"));
        sc = new Scanner(reader);
        parser = new Parser(sc);
        Emitter e = new Emitter("emitter.asm");
        parser.parseProgram().compile(e);

        // Output a space between the Interpreters results for the 2 tests.
        System.out.println();
        System.out.println();

        // Tests "ParserTestAdvanced.txt" as an Interpreter
        reader = new FileInputStream(new File("ParserTestAdvanced.txt"));
        sc = new Scanner(reader);
        parser = new Parser(sc);
        env = new Environment(null);
        parser.parseProgram().exec(env);

        // Tests "ParserTestAdvanced.txt" as a Compiler
        reader = new FileInputStream(new File("ParserTestAdvanced.txt"));
        sc = new Scanner(reader);
        parser = new Parser(sc);
        e = new Emitter("emitterAdvanced.asm");
        parser.parseProgram().compile(e);
    }
}
