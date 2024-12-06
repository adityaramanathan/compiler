package ast;

import java.io.*;
import java.util.List;

/**
 * The class that handles the production of instructions that have been
 * converted from Pascal to MIPS.
 *
 * Usage:
 * Emitter e = new Emitter(<filename>);
 *
 * @author Anu Datar, Aditya Ramanathan
 * @version 5/24/24
 */
public class Emitter
{
	private PrintWriter out;
	private static int label;
	private ProcedureDeclaration currProc;
	private int numElementsInStack;

	/**
	 * creates an emitter for writing to a new file with given name.
	 * @param outputFileName the name of the output file.
	 */
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * prints one line of code to file (with non-labels indented).
	 * @param code the line of code to be printed.
	 */
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}

	/**
	 * closes the file. Should be called after all calls to emit.
	 */
	public void close()
	{
		out.close();
	}

	/**
	 * Pushes the value in the register to the stack in memory.
	 * @param reg the name of the register that contains the value
	 * that needs to be pushed to the stack.
	 */
	public void emitPush(String reg)
	{
		emit("subu $sp $sp 4");
		emit("sw " + reg + " ($sp)");
		numElementsInStack++;
	}

	/**
	 * Pops the value in the register from the stack in memory.
	 * @param reg the name of the register that contains the value
	 * that needs to be popped from the stack.
	 */
	public void emitPop(String reg)
	{
		emit("lw " + reg + " ($sp)");
		emit("addu $sp $sp 4");
		numElementsInStack--;
	}

	/**
	 * Returns the next number label id.
	 * @return the next number from the previous time this method
	 * was called.
	 */
	public int nextLabelID()
	{
		label++;
		return label;
	}

	/**
	 * Remembers proc as the current procedure context.
	 * @param proc the procedure to remember as the current procedure
	 * context.
	 */
	public void setProcedureContext(ProcedureDeclaration proc)
	{
		currProc = proc;
	}

	/**
	 * Clears the current procedure context.
	 * @postcondition currProc is null.
	 */
	public void clearProcedureContext()
	{
		currProc = null;
	}

	/**
	 * Determines whether the variable is a local variable or not.
	 * @param varName the name of the variable.
	 * @return true if the variable is a local variable, false otherwise.
	 */
	public boolean isLocalVariable(String varName)
	{
		if(currProc == null)
		{
			return false;
		}
		if(varName.equals(currProc.getName()))
		{
			return true;
		}
		for(String var: currProc.getLocalVariables())
		{
			if(var.equals(varName))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines the offset from $sp for a specific local variable.
	 * @param localVarName the name of the local varable for which
	 * the location in the stack is wanted.
	 * @precondition localVarName is the name of a variable that is
	 * local to the procedure currently being compiled.
	 * @return the offest for the specific local variable.
	 */
	public int getOffset(String localVarName)
	{
		List<String> localVars = currProc.getLocalVariables();
		int stackSize = (localVars.size() + numElementsInStack) * 4;
		int indexMatch = 0;

		for(int i = 0; i < localVars.size(); i++)
		{
			if(localVarName.equals(localVars.get(i)))
			{
				indexMatch = i;
				break;
			}
		}
		return stackSize - (indexMatch * 4);
	}
}
