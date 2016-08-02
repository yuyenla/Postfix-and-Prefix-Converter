//Y-Uyen La, CS240, Project 4: Stacks
//The PreFix class takes care of transforming infix expression to a prefix expression.
//The PostFix class takes care of transforming infix expression to a postfix expression.
//These classes are capable of identifying incorrect input.

import java.util.Scanner;
class PreFix
{
    private NodeStack operator;
    private NodeStack operand;
    private String input;
	int operatorcount = 0; //check to see if you're missing an operator
    int operandcount = 0; //check to see if you're missing an operand
	Boolean valid = true;

    public PreFix(String s)
    { 
       input = s; 
    }

    public String doPre() throws Exception //does work to convert infix to prefix
    {
        operator = new NodeStack(); // makes an operator stack
        operand = new NodeStack();  //makes an operand stack        
        char ch; // for input, when the string gets split into characters
        int parenthesis = 0; //to make sure if the parenthesis are matched
        String result = "";
        String rightoperand = "";
        String leftoperand = "";
        String operatorhold = "";
        String operatorleftright = "";
	
		
        
        for(int i=0; i<input.length(); i++)       
        {
            String temp=""; 
            ch = input.charAt(i);   
            temp += ch; //converts ch to string for pCheck & for compatability with the nodes.
            
            if(ch >= '0' && ch <= 'z')  //if it's just the variable or number, push it onto the operand stack.      
            {							//this is under the assumption that only letters and single digits are entered, as per in the project specs.
                operand.push(temp); 
                operandcount++;
				valid = true;
            }
            else if (ch == ' ') //handles blank spaces in the input string
            {
            }
            else if(ch == '(') //push left operator on stack. & don't do operatorcount for parentheses
            {
                operator.push(ch);
				valid = false;
                parenthesis = parenthesis+1;
            }
            else if(ch == ')' && parenthesis == 0) //in the case that there isn't an opening '('
            {
                parenthesis = parenthesis-1;
            }
            else if(ch == ')' && !operator.top().equals("(")) //if you see a closing ")", pop of operators and operands to form expressions and push them
            {                                                 //onto the operand stack, once this expression is complete, the parentheses are popped off.
                while(!operator.top().equals("("))
                {
                    operatorhold = operator.pop();
                    operatorcount--;
                    rightoperand = operand.pop();
                    operandcount--;
                    leftoperand = operand.pop();
                    operandcount--;
                    operatorleftright = operatorhold + leftoperand + rightoperand;
                    operand.push(operatorleftright);
                    operandcount++;
					valid = true;
                }
                if(operator.top().equals("(")) //if left operand is at the top, pop off.
                {
                    operator.pop();
                }
                parenthesis = parenthesis-1;
            }
            else if(operator.isEmpty()) //if there's nothing on the stack, push the first operator it sees.
            {
                operator.push(ch);
                operatorcount++;
				valid = false;
            }
            else if(pCheck(operator.top(), temp)) //if the precendence of temp is higher than the top of the stack, then push temp.
            {                          
                operator.push(ch);
                operatorcount++;
				valid = false;
            }
            else if(!pCheck(operator.top(), temp)) //if the precendence of temp is lower/equal than the top of the stack, then push temp.
            {                          
                while(!pCheck(operator.top(), temp))
                {
                    operatorhold = operator.pop();
                    operatorcount--;
                    rightoperand = operand.pop();
                    operandcount--;
                    leftoperand = operand.pop();
                    operandcount--;
                    operatorleftright = operatorhold + leftoperand + rightoperand; //this allows to to print out things in the right order
                    operand.push(operatorleftright);
                    operandcount++;
                }
                operator.push(temp);
                operatorcount++;
				valid = false;
            }
        }

         if(parenthesis == 0) //if no mismatched parentheses....
        {
            String tempResult = "";
           
           if(operandcount <= operatorcount) //only checks if operand is missing
                return "\nERROR: operand is missing. (in prefix)";
           if(operandcount - operatorcount > 1) //only checks if operator is missing
                return "\nERROR: operator missing. (in prefix)";
           if(!operator.isEmpty() && operandcount > operatorcount) //if all is well, just pop off everything.
           {
               while(!operator.isEmpty())
                {
                    operatorhold = operator.pop(); 
                    rightoperand = operand.pop(); 
                    leftoperand = operand.pop(); 
                    operatorleftright = operatorhold + leftoperand + rightoperand;
                    operand.push(operatorleftright);
                }
                result += operand.pop();
           }
           else //if operator is empty (meaning that the expression didn't use parentheses)
           {
               result += operand.pop();
           }
           return "Prefix: " + result; //returns the correct output
        }
        else //this checks for in the case that 2 things are missing, or if the only thing wrong is the mismatched parentheses
        {
            int check = operandcount-operatorcount;
            if(check > 1)
            {   
                result = "\nERROR: operator missing & mismatched parentheses. (in prefix)";
            }
            else if(operatorcount >= operandcount && check <= 0)
                result = "\nERROR: operand is missing & mismatched parentheses. (in prefix)";
            else
                result =  "\nERROR: Parenthesis mismatch. (in prefix)";
            return result;
        }
    }
    
    public Boolean pCheck(String top, String op) //checks precedence
    {
        int check1 = 0;
        int check2 = 0;
        if(top.equals("+") || top.equals("-"))
            check1 = 1;
        if(op.equals("+") || op.equals("-"))
            check2 = 1;
        if(top.equals("*") || top.equals("/"))
            check1 = 2;
        if(op.equals("*") || op.equals("/"))
            check2 = 2;
        if(check1 < check2) //if top has a lower precedence, then pop the top and then push op
            return true;
        return false;
    }  
}  //endclass PreFix

class PostFix
{
    private NodeStack operator;
    private String input;
	public Boolean valid = true;
	int operatorcount = 0; //check to see if you're missing an operator
    int operandcount = 0; //check to see if you're missing an operand

    public PostFix(String s)
    { 
       input = s; 
    }

    public String doPost() throws Exception
    {
        operator = new NodeStack();             // make new stack
        char ch; //a,b,c,d
        char op; //operand
        
        String result = "";
        int parenthesis = 0;
        
        for(int i=0; i<input.length(); i++)       
        {
            String temp=""; 
            ch = input.charAt(i);   
            temp += ch; //converts ch to string for pCheck.
            if(ch >= '1' && ch <= 'z')        //for postfix, if it's a an operand, print it out. 
            {
                result += ch;
				valid = true;
                operandcount++;
            }
            else if (ch == ' ') //handles blank spaces
            {
            }
            else 
            {
                if(ch == '(')
                {
                    parenthesis++;
                    operator.push(ch);
					valid = false;
                    //don't count operator here.
                }
                else if(ch == ')' && !operator.isEmpty()) //if you see a ")" and there's stuff on the stack, perform opeations (build the expression)
                {
                    while(!operator.top().equals("(") && !operator.isEmpty() )
                    {
                        result += operator.pop();
                    }
                    if(!operator.isEmpty())
                        operator.pop();
                    parenthesis--;
                }
                else if(ch == ')') //in the case there's just a ")" there
                {
                    parenthesis--;
                }
                else if(operator.isEmpty()) //if the stack is empty, just push the operator
                {   
                    operator.push(ch);
                    operatorcount++;
					valid = false;
                }
                else if(pCheck(operator.top(), temp)) //if the precendence of temp is higher than the top of the stack, then push temp.
                {                          
                    operator.push(ch);
                    operatorcount++;
					valid = false;
                }
                else if(!pCheck(operator.top(), temp))
                {
                    while(!pCheck(operator.top(), temp)) //if precedence of temp is lower than top of stack, keep on popping stuff off and then push temp/ch
                    {
                        result += operator.pop();
                    }
                    operator.push(ch);
                    operatorcount++;
					valid = false;
                }
                else
                {
                    System.out.print("equation is wrong");
                }
            } 
        }
        
        
        if(parenthesis == 0) //these checks are essecntailly the same as in prefix, only difference is how it prints out the expression correctly.
        {
            if(operandcount <= operatorcount)
                return "\nERROR: operand is missing. (in postfix)";
			if(operandcount - operatorcount > 1)
                return "\nERROR: operator missing. (in postfix)";
           
                while(!operator.isEmpty()) //cleans up stack.
                {       
                        result += operator.pop();
                }
                if(operandcount <= operatorcount)
                    result = "\nERROR: operand is missing. (in postfix)";
                if(operandcount - operatorcount > 1)
                    result = "\nERROR: operator missing. (in postfix)";
            
            return "\nPostfix: " + result; //returns the correct output
        }
        else
        {
            int check = operandcount-operatorcount;
            if(check > 1)
            {    
                result = "\nERROR: operator missing & mismatched parentheses. (in postfix)";
            }
            else if(operatorcount >= operandcount && check <= 0)
                result = "\nERROR: operand is missing & mismatched parentheses. (in postfix)";
            else
                result =  "\nERROR: Parenthesis mismatch. (in postfix)";
            return result;
        }
    }
    
    public Boolean pCheck(String top, String op) //checks precedence
    {
        int check1 = 0;
        int check2 = 0;
        if(top.equals("+") || top.equals("-"))
            check1 = 1;
        if(op.equals("+") || op.equals("-"))
            check2 = 1;
        if(top.equals("*") || top.equals("/"))
            check1 = 2;
        if(op.equals("*") || op.equals("/"))
            check2 = 2;
        if(check1 < check2) //if top has a lower precedence, then pop the top and then push op
            return true;
        return false;
    }
       
}  
//end class Fix
class PostFix 
{   
    public static void main(String[] args)  throws Exception
    {
        Scanner k = new Scanner(System.in);
        String postoutput;
        String preoutput;
        System.out.print("Please enter infix equation: ");
        String equation = k.nextLine();

        while(!equation.equals(""))	
        {
            System.out.print("Infix: " + equation);
            
            
            //postfix
            PostFix post = new PostFix(equation);
            postoutput = post.doPost();
			if(post.valid != true && post.operatorcount > post.operandcount)
			{
				System.out.print("\nError: Stack is empty. (in postfix)");
			}
			else
            System.out.print(postoutput);
            
            //prefix
            PreFix pre = new PreFix(equation);
            preoutput = pre.doPre();
			if(pre.valid != true && pre.operatorcount > pre.operandcount)
			{
				System.out.print("\nError: Stack is empty. (in prefix)");
			}
			else
				System.out.print("\n" + preoutput);
            
            System.out.print("\nEnter infix equation, or press enter to quit: ");
            equation = k.nextLine();
        }
    }
}
