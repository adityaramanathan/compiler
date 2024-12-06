package scanner;
import java.io.*;

/**
 * A Scanner is responsible for reading an input stream, one character at a time,
 * and separating the input into tokens which are one of the following:
 * 1. A number, which is comprised of digits.
 * 2. An identifier, which is a key word in the pascal language that is being scanned.
 * 3. An operand, which is a character of size one that belongs to the following array:
 * {'=', '+', '-', '*', '/', '%', '(' ,')',';', ':', '<', '>'},
 * or a token of size two that belongs to this array: {'>=', '<=', '<>', '!='}
 *
 * Usage:
 * FileInputStream reader = new FileInputStream(new File(<file name>));
 * Scanner scanner = new Scanner(reader);
 * Now using the nextToken() method, you can scan through the file token by token.
 *
 * @author Aditya Ramanathan
 * @version 2/1/24, comma ',' functionality added 4/16/24
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;

    /**
     * Scanner constructor for construction of a scanner that uses an InputStream
     * object for input.
     * Usage:
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * Scanner constructor for constructing a scanner that scans a given input
     * string. It sets the end-of-file flag an then reads the first character of
     * the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * The getNextChar method attempts to get the next character from the input
     * stream. It sets the eof flag true if the end of file is reached on
     * the input stream. Otherwise, it reads the next character from the stream
     * and converts it to a Java String object.
     * @postcondition the input stream is advanced one character if it is not at
     * end of file and the currentChar instance field is set to the String
     * representation of the character read from the input stream.  The flag
     * eof is set true if the input stream is exhausted.
     */
    private void getNextChar()
    {
        try
        {
            int inp = in.read();
            if(inp == '.' || inp == -1)
                eof = true;
            else
                currentChar = (char) inp;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Advances through the file and retrieves the next character by invoking
     * getNextChar().
     * @param chr the character that was found in the file that needs to be
     * compared to currentChar.
     * @postcondition the instance variable currentChar is a new char or the
     * program has exited through an Illegal argument exception.
     */
    private void eat(char chr)
    {
        if(chr == currentChar)
        {
            getNextChar();
        }
        else
        {
            throw new IllegalArgumentException(currentChar + " is not " + chr);
        }
    }

    /**
     * Determines whether there are more tokens in the input stream.
     * @return true if there is another token in the input stream, false otherwise.
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Determines whether the character given is a digit.
     * @param chr the character to determine whether it is a digit.
     * @return true if chr is a digit, false otherwise.
     */
    public static boolean isDigit(char chr)
    {
        return chr >= '0' && chr <= '9';
    }

    /**
     * Determines whether the character given is a letter.
     * @param chr the character to determine whether it is a letter.
     * @return true if chr is a letter, false otherwise.
     */
    private static boolean isLetter(char chr)
    {
        return (chr >= 'a' && chr <= 'z') || (chr >= 'A' && chr <= 'Z');
    }

    /**
     * Determines whether the character given is a white space.
     * @param chr the character to determine whether it is a white space.
     * @return true if chr is a white space, false otherwise.
     */
    public static boolean isWhiteSpace(char chr)
    {
        return chr == ' ' || chr == '\n' || chr == '\t' || chr == '\r';
    }

    /**
     * Determines whether the character given is an operand. The array operands
     * defined within the method defines all the possible operands.
     * @param chr the character to determine whether it is an operand.
     * @return true if chr is an operand, false otherwise.
     */
    public static boolean isOperand(char chr)
    {
        char[] operands = {'=', '+', '-', '*', '/', '%', '(' ,')',';', ':', '<', '>', ','};
        for (char operand: operands)
        {
            if (operand == chr)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans the characters in the input and if it is a number, returns that
     * number as the next token, otherwise, throws a ScanErrorException indicating
     * that the token was not a number.
     * @return the lexeme containing the number.
     * @throws ScanErrorException
     */
    private String scanNumber() throws ScanErrorException
    {
        if (!isDigit(currentChar))
        {
            throw new ScanErrorException("Not a number");
        }

        String number = "";

        while(isDigit(currentChar))
        {
            number += currentChar;
            eat(currentChar);
        }

        if (isWhiteSpace(currentChar) || isOperand(currentChar))
        {
            return number;
        }

        throw new ScanErrorException("Not a number");
    }

    /**
     * Scans the characters in the input and if it is an identifier, returns that
     * identifier as the next token, otherwise, throws a ScanErrorException indicating
     * that the token was not a identifier.
     * @return the lexeme containing the identifier.
     * @throws ScanErrorException
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String identifier = "";
        if (!isLetter(currentChar))
        {
            throw new ScanErrorException("Not an identifier");
        }

        while (isDigit(currentChar) || isLetter(currentChar))
        {
            identifier += currentChar;
            eat(currentChar);
        }

        if (isWhiteSpace(currentChar) || isOperand(currentChar))
        {
            return identifier;
        }

        throw new ScanErrorException("Not an identifier");
    }

    /**
     * Scans the currentChar instance variable and if it is an operand, returns that
     * operand as the next token, otherwise, throws a ScanErrorException indicating
     * that the token was not a operand.
     * @return the lexeme containing the operand.
     * @throws ScanErrorException
     */
    private String scanOperand() throws ScanErrorException
    {
        String operand = "";

        if (!isOperand(currentChar))
        {
            throw new ScanErrorException("Not an operand");
        }
        else
        {
            operand += currentChar;
            eat(currentChar);
            return operand;
        }
    }

    /**
     * Retrieves the next token in the input stream. It ignores (eats) whitespaces
     * and single line comments.
     * @return the next token in the input stream.
     */
    public String nextToken()
    {
        try
        {
            while(hasNext() && isWhiteSpace(currentChar))
            {
                eat(currentChar);
            }

            if(currentChar == '/')
            {
                char prev = currentChar;
                eat(currentChar);
                // handle comments
                if(currentChar == '/')
                {
                    while(currentChar != '\n')
                    {
                        eat(currentChar);
                    }
                    eat(currentChar);
                }
                else
                {
                    String operand = "" + prev;
                    return operand;
                }
            }
            while(hasNext() && isWhiteSpace(currentChar))
            {
                eat(currentChar);
            }
            if (eof)
            {
                return "EOF";
            }
            else if (isLetter(currentChar))
            {
                return scanIdentifier();
            }
            else if (isDigit(currentChar))
            {
                return scanNumber();
            }
            else if (isOperand(currentChar))
            {
                String operand = scanOperand();
                // check for multi-symbol operands
                if ((operand.equals("<") && (currentChar == '>' || currentChar == '='))
                        || (operand.equals(">") && (currentChar == '='))
                        || (operand.equals(":") && (currentChar == '=')))
                {
                    operand += currentChar;
                    eat(currentChar);
                }
                return operand;
            }
            throw new ScanErrorException("unrecognized character");
        }
        catch (ScanErrorException e)
        {
            e.printStackTrace();
            // System.exit(-1);
        }
        eat(currentChar);
        return "";
    }
}
