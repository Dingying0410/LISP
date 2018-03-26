# LISP
LISP interpreter  
The LISP Interpreter has the following functions:   
Scanner (Lexical Analyzer): Given the input (a sequence of ASCII characters), it will scan the input and print the output, reporting the number of open and closing parentheses, literal atoms and numeric atoms in the input expression. And it shows the literal atoms and the sum of the numeric atoms in the input.   

For example, given the input:  
(DEFUN F23 (X) (PLUS X 12 55))    
The output would be:  
LITERAL ATOMS: 5, DEFUN, F23, X, PLUS, X    
NUMERIC ATOMS: 2, 67     
OPEN PARENTHESES: 3  
CLOSING PARENTHESES: 3  
This means that there are 5 literal atoms and 2 numeric atoms, and the sum of the numeric atoms is 67. 

To test the sanner:
java Scanner <../../test/ScannerTest    
in LISP/src/main/java



