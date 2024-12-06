package parser;
import scanner.*;
import ast.*;
import ast.Number;

import java.util.List;
import java.util.ArrayList;

/**
 * A Parser is responsible for going through the stream of tokens output by the
 * Scanner and creating a abstract syntax tree of these tokens. It uses a basic set
 * of grammar rules to determine what the tokens mean in context of what it is parsing,
 * and creates the Abstract Syntax Tree (AST). Then, the AST is used either by the
 * Compiler or Interpreter to produce the output which is a MIPS program or the
 * output of the code into the terminal respectively. This is a top-down recursive
 * descent parser without backtracking.
 *
 * Usage for Compiler:
 * FileInputStream reader = new FileInputStream(new File(<file name>));
 * Scanner sc = new Scanner(reader);
 * Parser parser = new Parser(sc);
 * Emitter e = new Emitter("emitter.asm");
 * parser.parseProgram().compile(e);
 *
 * Usage for Interpreter:
 * FileInputStream reader = new FileInputStream(new File(<file name>));
 * Scanner sc = new Scanner(reader);
 * Parser parser = new Parser(sc);
 * Environment env = new Environment();
 * parser.parseProgram().exec(env);
 *
 * @author Aditya Ramanathan
 * @version 4/12/24, variables functionality in parseProgram() added 5/24/24
 */
public class Parser
{
    private Scanner sc;
    private String currToken;

    /**
     * Parser constructor for construction of a parser that uses a Scanner
     * object for input.
     * Usage:
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * Parser par = new Parser(lex);
     * @param sc the scanner that is being used
     */
    public Parser(Scanner sc)
    {
        this.sc = sc;
        currToken = sc.nextToken();
    }

    /**
     * Advances through the stream of tokens and retrieves the next
     * token by a call to the scanner's nextToken() method.
     * @param token the token that is expected next in the scanner.
     * @postcondition the instance variable currToken is a new token or the
     * program has exited through an illegal argument exception.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    private void eat(String token) throws IllegalArgumentException
    {
        if(currToken.equals(token))
        {
            currToken = sc.nextToken();
        }
        else
        {
            throw new IllegalArgumentException("Token expected: " + currToken + ", Token recieved:" + token);
        }
    }

    /**
     * Given that the current token (the instance variable currToken) is a
     * number, this method stores the value of the number, eats the token,
     * and returns a Number object storing the value of the number.
     * @precondition the current token is an integer.
     * @postcondition the current token has been eaten.
     * @return a Number object containing the value of the number.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    private Number parseNumber() throws IllegalArgumentException
    {
        int num = Integer.parseInt(currToken);
        eat(currToken);
        return new Number(num);
    }

    /**
     * This method parses some sort of mathematical term that might be
     * multiplied or divided. It uses the parseExpression() method
     * to parse factors within parenthesis. It also parses factors with a
     * negative sign in front and just numbers themselves. Lastly, it can parse
     * procedure calls and variable calls.
     * @precondition the current token is a "(", "-", an identifier, or an integer.
     * @postcondition the factor has been eaten fully and the component of the
     * abstract syntax tree that evaluates to the value of the factor is created.
     * @return an Expression object representing the parsed factor
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    public Expression parseFactor() throws IllegalArgumentException
    {
        if(currToken.equals(","))
        {
            eat(",");
        }

        if(currToken.equals("("))
        {
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            return exp;
        }

        if(currToken.equals("-"))
        {
            eat("-");
            return new BinOp("*", new Number(-1), parseFactor());
        }

        if((int)(currToken.charAt(0)) >= 48 && (int)(currToken.charAt(0)) <= 57)
        {
            return parseNumber();
        }

        String name = currToken;
        eat(name);
        ArrayList<Expression> args = new ArrayList<Expression>();
        if(currToken.equals("("))
        {
            eat("(");
            while(!currToken.equals(")"))
            {
                Expression exp = parseExpression();
                args.add(exp);
            }
            eat(")");
            return new ProcedureCall(name, args);
        }
        else
        {
            return new Variable(name);
        }
    }

    /**
     * This method parses some sort of mathematical term that might be
     * added or subtracted.
     * @return an Expression object representing the parsed term.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    public Expression parseTerm() throws IllegalArgumentException
    {
        Expression exp = parseFactor();
        while(currToken.equals("*") || currToken.equals("/") || currToken.equals("mod"))
        {
            if(currToken.equals("*"))
            {
                eat(currToken);
                exp = new BinOp("*", exp, parseFactor());
            }
            else if(currToken.equals("/"))
            {
                eat(currToken);
                exp = new BinOp("/", exp, parseFactor());
            }
            else
            {
                eat(currToken);
                exp = new BinOp("%", exp, parseFactor());
            }
        }
        return exp;
    }

    /**
     * Evaluates any expression consisting of other expressions and terms that
     * are joined by "+" or "-" operators.
     * @return an Expression object representing the parsed expression.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    public Expression parseExpression() throws IllegalArgumentException
    {
        Expression exp = parseTerm();
        while(currToken.equals("+") || currToken.equals("-"))
        {
            if(currToken.equals("+"))
            {
                eat(currToken);
                exp = new BinOp("+", exp, parseExpression());
            }
            else
            {
                eat(currToken);
                exp = new BinOp("-", exp, parseExpression());
            }
        }
        return exp;
    }

    /**
     * Parses a condition which exists after an "IF" or a "WHILE" token
     * in a pascal program in order to check whether the statements within
     * the IF condition or the WHILE loop should be executed.
     * @precondition the condition must have an expression, then a relop that
     * is one of ">", "<", ">=", "<=", "<>", "=", and then another expression.
     * If it sees an expression, it assumes the next token is a relop.
     * @return a Condition object representing the parsed condition.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    public Condition parseCondition() throws IllegalArgumentException
    {
        Expression exp1 = parseExpression();
        String relop = currToken;
        eat(currToken);
        Expression exp2 = parseExpression();
        return new Condition(exp1, relop, exp2);
    }

    /**
     * This is the method called on each token within the main block of code in
     * the program or within a procedure. The possible start tokens of a
     * statement are "BEGIN", "WHILE", "IF", "WRITELN", "READLN".
     * The statement could also be an initialization statement.
     * For "BEGIN", it parses all statements till "END" is seen.
     * For "WHILE", it parses the while loop and the statements within it.
     * For "IF", it parses the if condition, statement, and also the else
     * statement.
     * For "WRITELN", the method parses the expression within the WRITELN.
     * For "READLN", the method parses a user input and assigns it to the variable.
     * Otherwise, it will add the variable symbol and its initialization value to the
     * map if the symbol is not yet defined, and if the symbol is defined, it will
     * update the value.
     * @return a Statement object representing the parsed statement.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    public Statement parseStatement() throws IllegalArgumentException
    {
        if(currToken.equals("BEGIN"))
        {
            eat("BEGIN");
            ArrayList<Statement> stmts = new ArrayList<Statement>();
            while(!currToken.equals("END"))
            {
                stmts.add(parseStatement());
            }
            Block block = new Block(stmts);
            eat("END");
            eat(";");
            return block;
        }
        else if(currToken.equals("WHILE"))
        {
            eat("WHILE");
            Condition cond = parseCondition();
            eat("DO");
            Statement st = parseStatement();
            return new While(cond, st);
        }
        else if(currToken.equals("IF"))
        {
            eat("IF");
            Condition cond = parseCondition();
            eat("THEN");
            Statement st = parseStatement();
            if(currToken.equals("ELSE"))
            {
                eat("ELSE");
                Statement elseSt = parseStatement();
                return new If(cond, st, elseSt);
            }
            return new If(cond, st);
        }
        else if(currToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(exp);
        }
        else if(currToken.equals("READLN"))
        {
            eat("READLN");
            eat("(");
            String var = currToken;
            eat(currToken);
            eat(")");
            eat(";");
            return new Readln(var);
        }
        else // it is Assignment
        {
            String symbol = currToken;
            eat(currToken);
            eat(":=");
            Expression exp = parseExpression();
            Assignment assignment = new Assignment(symbol, exp);
            if(currToken.equals(";"))
            {
                eat(";");
            }
            return assignment;
        }
    }

    /**
     * Parses through a list of variables which are either at the
     * top of the pascal program file or the parameters of a procedure
     * and returns the list of these variables.
     * @return a list of the variables that have been parsed.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    public List<String> parseVars() throws IllegalArgumentException
    {
        ArrayList<String> vars = new ArrayList<String>();
        while(currToken.equals("VAR"))
        {
            eat("VAR");
            List<String> params = new ArrayList<String>();
            while(!currToken.equals(";"))
            {
                if(!currToken.equals(","))
                {
                    params.add(currToken);
                }
                eat(currToken);
            }
            eat(";");
            for(String var: params)
            {
                vars.add(var);
            }
        }
        return vars;
    }

    /**
     * Parses through the procedure declaration by first parsing through the
     * name, then the parameters, then the local variables to the procedure,
     * and lastly the statement(s) contained within the procedure.
     * @return the procedureDeclaration object corresponding to the specific
     * procedure.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    public ProcedureDeclaration parseProcedure() throws IllegalArgumentException
    {
        eat("PROCEDURE");
        String name = currToken;
        eat(name);
        eat("(");
        ArrayList<String> parms = new ArrayList<String>();
        while(!currToken.equals(")"))
        {
            if(!currToken.equals(","))
            {
                parms.add(currToken);
            }
            eat(currToken);
        }
        eat(")");
        eat(";");

        List<String> localVariables = parseVars();
        for(String p: parms)
        {
            localVariables.add(p);
        }

        Statement st = parseStatement();
        return new ProcedureDeclaration(name, st, parms, localVariables);
    }

    /**
     * This is the outermost parse method that parses through the whole program.
     * It begins by parsing through variables till it sees the currToken is no
     * longer "VAR". Then, it parses through procedures till it sees that the
     * currToken instance variable is no longer "PROCEDURE". Within the procedure,
     * it will parse all variables first. Next it parses the statement.
     * It adds both the list of variables, rocedure declarations and the statement to
     * a Program object and returns this. This Program object stores all the
     * information needed to execute the program.
     * @return the Program object containing all the information needed to execute
     * the program.
     * @throws IllegalArgumentException if currToken is not what is expected.
     */
    public Program parseProgram() throws IllegalArgumentException
    {
        // 1. parse global Variables
        List<String> vars = parseVars();

        // 2. parse Procedure declarations
        ArrayList<ProcedureDeclaration> procedureDecs = new ArrayList<ProcedureDeclaration>();
        while(currToken.equals("PROCEDURE"))
        {
            procedureDecs.add(parseProcedure());
        }

        // 3. parse Statement(s), the code below the next line allows for multiple statements that are not enclosed in a block.
        Statement st = parseStatement();

        List<Statement> stmts = new ArrayList<Statement>();
        while(sc.hasNext())
        {
            stmts.add(parseStatement());
        }

        if(stmts.size() == 0)
        {
            return new Program(procedureDecs, st, vars);
        }
        else
        {
            stmts.add(0, st); // put the first statement in
            return new Program(procedureDecs, new Block(stmts), vars);
        }
    }
}
