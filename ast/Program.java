package ast;
import environment.*;

import java.util.List;

/**
 * The Program class keeps track of the main body of code, all the procedures
 * declared in the program, and the global variables. As the program is parsed,
 * this information is collected and stored for later use during execution.
 * @author Aditya Ramanathan
 * @version 4/13/24
 */
public class Program
{
    private List<ProcedureDeclaration> procedureDecs;
    private List<String> vars;
    private Statement st;

    /**
     * Creates a Program with all the procedures and the statements in the main
     * block of code.
     * @param procedureDecs list of all procedures defined in the program.
     * @param st the statement(s) in the main block of code.
     * @param vars the variables defined at the top of the main block of code.
     */
    public Program(List<ProcedureDeclaration> procedureDecs, Statement st, List<String> vars)
    {
        this.procedureDecs = procedureDecs;
        this.st = st;
        this.vars = vars;
    }

    /**
     * The exec method executes the program and produces the intended output
     * to the terminal.
     * @param env the environment containing all the variables
     * needed to execute the statement.
     * @postcondition the procedures were all declared in the environment and
     * the program was executed.
     */
    public void exec(Environment env)
    {
        for(ProcedureDeclaration dec: procedureDecs)
        {
            dec.exec(env);
        }
        st.exec(env);
    }

    /**
     * Compiles the program by first compiling the variables, the program itself,
     * and lastly the procedures and produces the corresponding MIPS instructions
     * for each of these.
     * @param e the emitter used to emit the MIPS instructions.
     */
    public void compile(Emitter e)
    {
        e.emit(".data");
        e.emit("nl: .asciiz \"\\n\"");

        for(String var: vars)
        {
            e.emit(var + ": .word 0");
        }

        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        st.compile(e);
        e.emit("li $v0 10");
        e.emit("syscall");

        for(ProcedureDeclaration dec: procedureDecs)
        {
            dec.compile(e);
        }
        e.close();
    }
}
