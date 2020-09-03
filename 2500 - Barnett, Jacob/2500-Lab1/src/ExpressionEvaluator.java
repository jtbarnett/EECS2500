import java.util.Scanner;

public class ExpressionEvaluator
{
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		String user = null;
		String output;
		int OP;
		int check = 0;
		
		//Creating a cunstructor for the main class to send the user string through the conversion method
		ExpressionEvaluator i = new ExpressionEvaluator();
		boolean done = false;
		String test = "Invalid expression. Please try again.";
		
		System.out.println("Please enter an infix notation expression: ");
		user = input.nextLine();
		
		while(user != null)
		{
			//do-while loop to check if the user input is valid, if it is not valid then
			//prompt the user to enter a new expression
			do
			{
				output = i.coversionInfixtoPostfix(user);
				OP = i.execution(output);
			
				//checks if the user expression or if the final calculated value is invalid
				if(output.equals(test) || OP >= 2147483647 || OP <= -2147483648)
				{
					//checking to see what kind of error it is
					if(output.equals(test))
					{
						System.out.println("\nSorry, " + user + " is an invalid expression. Please restart the program and try again. \n");
						check = 1;
						break;
					}
					else
					{
						System.out.println("\nSorry, but you have enterted an invalid number that is out of the ranges of calculation. Please restart the program and try again. \n");
						check = 1;
						break;
					}
				}
				else
					done = true;
			}while(!done);
			
			//if the user has entered an invalid input, then break out of the loop and end the program.
			//the program will continue to run otherwise
			if(check == 1)
			{
				break;
			}
			else
			{
				System.out.println();
				System.out.println(user);
				System.out.println(output);
				System.out.println(OP);
			
				user = input.nextLine();
			}
		}
	}
	
	public String coversionInfixtoPostfix(String userIn)
	{
	//This method is used to convert from infix notation to postfix notation
		
		LinkedStack stack = new LinkedStack();
		char temp;
		String outp = "";
		char cur;
		char paren = '(';
		String s = userIn + " ";
		
		//iterating through each character in the string of the infix expression
		for(int i = 0; i < s.length(); i++)
		{
			temp = s.charAt(i);
			
			//if character is a digit, then add it to the output string
			if(Character.isDigit(temp))
			{	
				char digit;
				int count = 1;
				
				//if the nect character is a digit, then it the number is a multi digit
				//number and not a single
				if(Character.isDigit(s.charAt(i + count)))
				{
					outp += temp;
					while(Character.isDigit(s.charAt(i + count)))
					{
						digit = s.charAt(i + count);
						outp += digit;
						count++;
					}
					i += count - 1;
					outp += " ";
				}
				else
					outp += temp + " ";
			}
			//if there is a blank space in the expression, skip it and move to the next iteration
			else if(temp == ' ')
				
				//making sure that there isn't an operator left on the stack at the end of the conversion
				if(i == s.length() - 1)
				{
					if(stack.top() != 0)
					{
						outp += stack.top();
					}
				}
				else
					continue;
			
			//if there is a left parentheses, push it to the stack
			else if(temp == '(')
				stack.push(temp);
			else if(temp == ')')
			{
				cur = stack.top();
				
				//this is the invalid case for a right parentheses
				if(temp == cur)
				{
					outp = "";
					outp = "Invalid expression. Please try again.";
					return outp;
				}
				else if(cur == '(')
				{
					stack.pop();
					cur = stack.top();
					
					//checks if there is an operator at the top of the stack. If there
					//is one, then it is alright to proceed. If not, then the expression
					//must be of invalid form.
					if(cur == '-' || cur == '+' || cur == '*' || cur == '/' || cur == '^' || cur == 'Q' || cur == 'C' || cur == '<' || cur == '>' || cur == '%')
						continue;
					if(cur == 0)
					{
						if(i != s.length() - 2)
						{
							continue;
						}
						else
						{
							outp = "";
							outp = "Invalid expression. Please try again.";
							return outp;
						}
					}
					else
					{
						outp = "";
						outp = "Invalid expression. Please try again.";
						return outp;
					}
				}
				else
				{
					outp += cur + " ";
					stack.pop();
					stack.pop();
				}
			}
			
			//Processing the valid operators
			else if(temp == '-' || temp == '+' || temp == '*' || temp == '/' || temp == '^' || temp == 'Q' || temp == 'C' || temp == '<' || temp == '>' || temp == '%')
			{
				//get the current top value on the stack
				if(stack.top() != paren)
				{
					if(stack.top() != 0)
					{
						//if the top of the stack has an operator and the next value is a Q or C operator
						//then we want to check certain conditions to see if the expression is valid
						if(temp == 'Q' || temp == 'C')
						{
							char digit;
							int count = 1;
							
							//if the value next to the Q or C is a digit, then we check to see how long it is and add it to the output string
							if(Character.isDigit(s.charAt(i + count)))
							{	
								while(Character.isDigit(s.charAt(i + count)))
								{
									digit = s.charAt(i + count);
									outp += digit;
									count++;
								}
								
								i += count - 1;
								outp += " ";
							}
							
							//if the value next to the Q or C is a left parentheses, then check to see if there is digits coming after it
							else if(s.charAt(i + count) == '(')
							{
								++count;
								if(Character.isDigit(s.charAt(i + count)))
								{	
									while(Character.isDigit(s.charAt(i + count)))
									{
										digit = s.charAt(i + count);
										outp += digit;
										count++;
									}
									
									i += count - 1;
									outp += " ";
								}
							}
							
							//the expression is invalid if none of the conditions are met
							else
							{
								outp = "";
								outp = "Invalid expression. Please try again.";
								return outp;
							}
							
							//we can finally add the Q or C to our output string after we have already added or numerical value
							outp += temp + " ";
						}
						else
						{
						//add the current top operator to the output string
						//pop the current top operator and then push the new temporary one onto the stack
						outp += stack.top() + " ";
						stack.pop();
						stack.push(temp);
						}
					}
					else if(stack.top() == 0)
						stack.push(temp);
					else
						continue;
				}
				else if(stack.top() == paren)
					
					//if the current character on the stack is a left parentheses, then it
					//is okay to push the operator onto the stack
					stack.push(temp);
			}
			
			//In this case, the character must be invalid. Thus, return the invalid
			//statement to the main method
			else
			{
				outp = "";
				outp = "Invalid expression. Please try again.";
				return outp;
			}
		}
		
		//return the final string output which converted the infix notation to postfix
		return outp;
	}
	
	public int execution(String element)
	{
	//this method is used to find the numerical value of the postfix notation
		
		//integer stack that is used to store the numerical values and then pop them to assign to operand variables
		LinkedIntStack array = new LinkedIntStack();
		int operand1;
		int operand2;
		int result = 0;
		char holder;
		int invalid = 2147483647;
		int shift;
		
		//traverse through the postfix notation string
		for(int i = 0; i < element.length(); i++)
		{
			holder = element.charAt(i);
			
			if(Character.isDigit(holder))
			{
				int count = 1;
				int stackValue;
				char digit;
				String temp = "";
				
				//checks to see if the next character is a digit, if so, the number must be a
				//multi digit number.Thus, we add it the existing number
				if(Character.isDigit(element.charAt(i + count)))
				{
					temp += holder;
					while(Character.isDigit(element.charAt(i + count)))
					{
						digit = element.charAt(i + count);
						temp += digit;
						count++;
					}
					
					//once we have found the length of the current digit, we need to
					//increase the digit i for however many times the while loop ran
					i += count - 1;
					try
					{
					stackValue = Integer.parseInt(temp);
					}
					catch(Exception e)
					{
						return invalid;
					}
					array.push(stackValue);
				}
				
				//if the next character is not a multi digit, then add the current digit to the stack
				else
				{
					temp += holder;
					stackValue = Integer.parseInt(temp);
					array.push(stackValue);
				}
			}
			
			//if the next character is a binary function
			else if(holder == '-' || holder == '+' || holder == '*' || holder == '/' || holder == '^' || holder == '<' || holder == '>' || holder == '%')
			{
				
				//is used to return the second operand from the stack
				if(array.isEmpty())
					return invalid;
				operand2 = array.top();
				array.pop();
				
				//used to return the first operand from the stack
				if(array.isEmpty())
					return invalid;
				operand1 = array.top();
				array.pop();
				
				//if there is 2 valid operands, then search for the corresponding operator
				//and perfrom the oepration
				if(holder == '-')
					result = operand1 - operand2;
				else if(holder == '+')
					result = operand1 + operand2;
				else if(holder == '*')
					result = operand1 * operand2;
				else if(holder == '/')
					if(operand2 != 0)
						result = operand1 / operand2;
					else
						return invalid;
				else if(holder == '^')
					result = (int) Math.pow(operand1, operand2);
				else if(holder == '%')
					if(operand2 != 0)
						result = operand1 % operand2;
					else
						return invalid;
				else if(holder == '<')
				{
					shift = (int) Math.pow(2, operand2);
					result = operand1 * shift;
				}
				else if(holder == '>')
				{
					shift = (int) Math.pow(2, operand2);
					result = operand1 / shift;
				}
				
				//push the final result of the operation onto the stack as a new operand
				array.push(result);
			}
			
			//if the next character is a unary function
			else if(holder == 'Q' || holder == 'C')
			{
				if(array.isEmpty())
					return invalid;
				operand1 = array.top();
				array.pop();
				
				if(holder == 'Q')
					result = (int) Math.sqrt(operand1);
				else if(holder == 'C')
					result = (int) Math.cbrt(operand1);
				
				array.push(result);
			}
			else
				continue;
		}
		
		//checks to see if there is a final result, if so it is the overall final calculation
		//of the postfix notation
		if(array.isEmpty())
			return invalid;
		result = array.top();
		array.pop();
		
		//checks if the stack is not empty, if it is, that means there is too many
		//operands and not enough operators. In this case, the postfix notation is invalid
		if(!array.isEmpty())
			return invalid;
		
		//finall, return the final result of the postfix notation
		return result;
	}
}
