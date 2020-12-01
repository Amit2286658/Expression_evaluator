import java.util.Scanner;
/**
 * @author Amit kumar.
 * execution on sololearn can take upto 30 ms more,
 * compared to execution on local machines.
 */
public class ExpressionEvaluator {
    
    static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        String expression = scan.nextLine();
        long startTime = System.nanoTime();
        System.out.println(Evaluate(expression));
        long endTime = System.nanoTime();
        System.out.println("Time taken for execution : "+(endTime-startTime)/1000000+" ms");
    }
    
    enum operations{
        /*create custom operators here, see SINE, MODULUS, FACTORIAL for example, as they were added 
        log10 and log_e and log_d was added on 19/11/2020,
          later on without touching any part of the codebase.
          add a case in #function as needed and return the appropriate value for the custom 
          operators.
        * the third parameter is the type, pass TYPE.POST_TYPE if the operator works with the 
          post operator value, like trigonometric functions;
          or pass TYPE.PRE_TYPE if the operator works with the pre operator value, 
          like factorial.
        * the function name is for convenience, like instead of using S30 or S(30) for sine function,
          with the name defined, you can use the name instead like sin is defined for sine function,
          which means now we can give input in this form sin(30) or sin30 or sin(-30) or sin-30.
          all names are converted into the lowercase. so it doesn't matter, if the given name is 
          in capital or small, it will be converted to small anyways.
        * TYPE.FUNCTION will be processed on top of everything, therefore there precedence is 0,
          for they do not conflict with any other operator.*/
        ADD('+', 1),
        SUBTRACT('-', 1),
        MULTIPLY('*', 2, "x"),
        DIVIDE('/', 2),
        EXPONENT('^', 3),
        SINE('S', 4, TYPE.POST, "sin"),
        COSINE('C', 4, TYPE.POST, "cos"),
        TANGENT('T', 4, TYPE.POST, "tan"),
        MODULUS('%', 3),
        FACTORIAL('!', 5, TYPE.PRE), 
        HYPOTENUSE('H', TYPE.FUNCTION, "hypot", 2, new int[]{
            operations.ARGUMENT_DOUBLE,
            operations.ARGUMENT_DOUBLE
        }),
        ROOT('√', 5),
        SUMMATION('B', TYPE.FUNCTION, "sum", 3, new int[]{
            operations.ARGUMENT_DOUBLE,
            operations.ARGUMENT_DOUBLE,
            operations.ARGUMENT_STRING
        }),
        ADDALL('A', TYPE.FUNCTION, "add"),
        LOG_10('W', 5, TYPE.POST, "log10"),
        LOG_E('L', 5, TYPE.POST, "ln"),
        LOG_D('V', 5, "log");
        /*d1 and d2 are the double values that are just before and after the operator.
          either d1 or d2 would be just '1' if the operator is either TYPE.POST_TYPE or 
          TYPE.PRE_TYPE respectively, values from d1 or d2 can be ignored in such case 
          or just be multiplied since, they would be just '1', they will have no effect on the result.
        * for example sine function works on post operator logic, like sin30 where sine function
          work on 30 which is after the operator, therefore post operator.
          in factorial like 5!, the factorial works on 5 which is before the operator, therefore
          pre operator.*/
        public double function(double d1, double d2){
            switch(operator){
                case '+' :
                    return d1 + d2;
                case '-' :
                    return d1 - d2;
                case '*' :
                    return d1 * d2;
                case '/' :
                    return d1 / d2;
                case '^' :
                    return Math.pow(d1, d2);
                case 'S' :
                    return d1 * ((Math.sin(d2*(Math.PI/180))));
                case 'C' :
                    return d1 * ((Math.cos(d2*(Math.PI/180))));
                case 'T' :
                    return d1 * ((Math.tan(d2*(Math.PI/180))));
                case '!' :
                    return factorial((int)d1) * d2;
                case '√' :
                    return Math.pow(d2, 1/d1);
                case '%' :
                    return d1 % d2;
                case 'V' :
                    return (Math.log(d2)/Math.log(d1));
                case 'W' :
                    return Math.log10(d2);
                case 'L' :
                    return Math.log(d2);
            }
            return 0;
        }
        
        /*
        * this function will be called when the type is TYPE.FUNCTION,
          it returns all the values seperated by comma inside the parentheses,
          parentheses is important to encapsulate the arguments of the function.
          
        */
        public double function(argument[] arguments){
            switch(operator){
                case 'H' :
                    return Math.sqrt(Math.pow(arguments[0].double_value, 2) + 
                            Math.pow(arguments[1].double_value, 2));
                case 'B' :
                    return summation(arguments[0].double_value, arguments[1].double_value,
                            arguments[2].string_value);
                case 'A' :
                    double d = 0;
                    for(argument ar : arguments){
                        d += ar.double_value;
                    }
                    return d;
            }
            return 0;
        }
        
        //that was all for the custom operator,
        //the codes below and everything else, does the rest of the work.
        public enum TYPE {
            PRE, POST, NONE, FUNCTION
        }
        
        public static final int ARGUMENT_DOUBLE = 1;
        public static final int ARGUMENT_STRING = 2;
        
        public final char operator;
        public final int precedence;
        public final int neutral_value = 1;
        public TYPE operator_type = operations.TYPE.NONE;
        public String functionName = null;
        public int argumentSize = -1;
        public int[] argument_type = {
            ARGUMENT_DOUBLE    
        };
        
        operations(char operator, int precedence){
            this.operator = operator;
            this.precedence= precedence;
        }
        
        operations(char operator, int precedence, TYPE type){
            this(operator, precedence);
            this.operator_type = type;
        }
        
        operations(char operator, int precedence, TYPE type, String functionName){
            this(operator, precedence, type);
            functionName = functionName.toUpperCase();
            this.functionName = functionName;
        }
        
        operations(char operator, int precedence, String functionName){
            this(operator, precedence, TYPE.NONE);
            functionName = functionName.toUpperCase();
            this.functionName = functionName;
        }
        
        operations(char operator, TYPE type, String functionName){
            this(operator, 0, type, functionName);
        }
        
        operations(char operator, TYPE type, String functionName, int argumentSize){
            this(operator, 0, type, functionName);
            this.argumentSize = argumentSize;
        }
        operations(char operator, TYPE type, String functionName, int argumentSize, int[] argumentType){
            this(operator, type, functionName, argumentSize);
            this.argument_type = argumentType;
        }
        
        public boolean evaluateSymbol(char symbol){
            return operator == symbol;
        }
        
        public boolean isFunction(char symbol){
            if(evaluateSymbol(symbol)){
                if (operator_type == TYPE.FUNCTION)
                    return true;
            }
            return false;
        }
        
        //nothing of interest here.
        public void beginFunction(char atStart, stepData step_data){
            for (operations op : operations.values()){
                if(op.evaluateSymbol(atStart)){
                    switch(op.operator){
                        case '+' :
                            step_data.setStepData("");
                            return;
                        case '-' :
                            step_data.setStepData("-");
                            return;
                        default :
                            throw new IllegalArgumentException("illegal start of expression");
                    }
                }
            }
            step_data.setStepData(String.valueOf(atStart));
        }
        
        //nothing of interest here either.
        public void endFunction(char atEnd, stepData step_data){
            for (operations op : operations.values()){
                if (op.evaluateSymbol(atEnd)){
                    switch(op.operator){
                        default : throw new IllegalArgumentException("illegal end of expression");
                    }
                }
            }
        }
        
        static class argument{
            public double double_value = 0;
            public String string_value = "";
            public int argument_type = 1;
            
            public argument(double double_value, String string_value, int argument_type){
                this.double_value = double_value;
                this.string_value = string_value;
                this.argument_type = argument_type;
            }
        }
    }
    
    //default functions
    public static double factorial(int factor){
        if (factor > 1)
            return factor * factorial(factor - 1);
        else return 1;
    }
    
    public static double summation(double initialBound, double finalBound, String expression){
        double exp = 0;
        char variable_char = 'a';
        for(char c : expression.toCharArray()){
            if(Character.isLetter(c)){
                variable_char = c;
                break;
            }
        }
        for(int i = (int)initialBound; i <= (int)finalBound; i++){
            exp += Double.parseDouble(Evaluate(expression.replaceAll(
                    String.valueOf(variable_char), String.valueOf(i))));
        }
        return exp;
    }
    //end of default functions
    
    public static String Evaluate(String expression){
        expression = expression.trim();
        expression = expression.replaceAll("\\s+", "");
        expression = expression.toUpperCase();
        expression = expression.replaceAll("\\)\\(", "\\)*\\(");
        
        String functionList = "";
        for(operations op : operations.values()){
            if(op.functionName != null){
                functionList += (functionList.length() != 0 ? "/" : "")
                        +op.functionName+"."+op.operator;
            }
        }
        
        if(!functionList.isEmpty()){
            if(functionList.contains("/")){
                String[] names = functionList.split("/");
                for(int i = 0; i < names.length; i++){
                    for(int j = i+1; j < names.length; j++){
                        String tempi = names[i];
                        String tempj = names[j];
                        if(tempj.length() > tempi.length()){
                            names[i] = names[j];
                            names[j] = tempi;
                        }
                    }
                }
                for(String str : names){
                    String[] args = str.split("\\.");
                    if(expression.contains(args[0])){
                        expression = expression.replaceAll(args[0], args[1]);
                    }
                }
            }else{
                String[] args = functionList.split("\\.");
                    if(expression.contains(args[0])){
                        expression = expression.replaceAll(args[0], args[1]);
                    }
            }
        }
        
        String functionOperators = "";
        for(operations op : operations.values()){
            if(op.functionName != null && op.operator_type == operations.TYPE.FUNCTION){
                functionOperators += (functionOperators.length() != 0 ? "|" : "") + op.operator;
            }
        }
        
        String str = "";
        for (int i = 0; i < expression.length(); i++){
            if(i != 0 && expression.charAt(i) == '(' &&
                    (String.valueOf(expression.charAt(i-1))).matches("1|2|3|4|5|6|7|8|9|0")){
                str += '*';
                str += expression.charAt(i);
            }else if(i != expression.length()-1 && expression.charAt(i) == ')' &&
                    (String.valueOf(expression.charAt(i+1))).matches("1|2|3|4|5|6|7|8|9|0")){
                str += expression.charAt(i);
                str += '*';
            }else if(i != expression.length() - 1 && expression.charAt(i) == ')' && 
                    functionOperators.length() != 0 && 
                    (String.valueOf(expression.charAt(i+1))).matches(functionOperators)){
                str += expression.charAt(i);
                str += '*';
            }else {
                str += expression.charAt(i);
            }
        }
        
        expression = str;
        
        expression = evaluateFunction(expression);
        
        boolean containsFunction = false;
        for(operations op : operations.values()){
            if(op.operator_type == operations.TYPE.FUNCTION){
                containsFunction = expression.contains(op.functionName);
            }
        }
        if(containsFunction)
            expression = Evaluate(expression);
        
        return evaluate(expression, true);
    }
    
    public static String evaluateFunction(String expression){
        operations whichOperator = null;
        boolean isInFunction = false;
        boolean isInBracket = false;
        int bracketCounter = 0;
        
        String subString = "";
        String str = "";
        
        outerloop :
        for(int i = 0; i < expression.length(); i++){
            if(!isInFunction){
                for(operations op : operations.values()){
                    if(op.operator_type == operations.TYPE.FUNCTION){
                        if(op.evaluateSymbol(expression.charAt(i))){
                            whichOperator = op;
                            isInFunction = true;
                            if(i != 0 && String.valueOf(expression.charAt(i-1)).matches("1|2|3|4|5|6|7|8|9|0")){
                                str += '*';
                            }
                            continue outerloop;
                        }
                    }
                }
            }
            if(isInFunction){
                if(expression.charAt(i) == '(' && !isInBracket){
                    isInBracket = true;
                    subString = "";
                    continue;
                }
                
                if(isInBracket){
                    switch (expression.charAt(i)) {
                        case ')':
                            if (bracketCounter == 0)
                                isInBracket = false;
                            else{
                                bracketCounter--;
                                subString += expression.charAt(i);
                            }
                            break;
                        case '(':
                            bracketCounter++;
                            subString += expression.charAt(i);
                            break;
                        default:
                            subString += expression.charAt(i);
                            break;
                    }
                }
                if(isInFunction && !isInBracket){
                    assert whichOperator != null;
                    String[] array = subString.split(",");
                    operations.argument[] arguments = new operations.argument[array.length];
                    if(whichOperator.argumentSize != -1 && 
                            whichOperator.argumentSize != arguments.length){
                        throw new IllegalStateException("the provided argument length does"
                                + " not match the required length of the argument");
                    }
                    for (int j = 0; j < array.length; j++) {
                        assert arguments != null;
                        if(whichOperator.argumentSize == -1){
                            arguments[j] = new operations.argument(Double.parseDouble(Evaluate(array[j])),
                                    "", operations.ARGUMENT_DOUBLE);
                        }else{
                            if(whichOperator.argument_type.length > 1){
                                switch (whichOperator.argument_type[j]) {
                                    case operations.ARGUMENT_DOUBLE:
                                        arguments[j] = new operations.argument(Double.parseDouble(Evaluate(array[j])),
                                                "", operations.ARGUMENT_DOUBLE);
                                        break;
                                    case operations.ARGUMENT_STRING:
                                        arguments[j] = new operations.argument(0, array[j], operations.ARGUMENT_STRING);
                                        break;
                                    default:
                                        throw new IllegalStateException("the given argument type is illegal");
                                }
                            }else{
                                arguments[j] = new operations.argument(Double.parseDouble(Evaluate(array[j])),
                                            "", operations.ARGUMENT_DOUBLE);
                            }
                        }
                    }
                    double d = whichOperator.function(arguments);
                    str += d;
                    isInFunction = false;
                }
            }else{
                str += expression.charAt(i);
            }
        }
        return str;
    }
    
    public static String evaluate(String expression, boolean includeSimpleFunction){
        Stack<Double> operand = new Stack<>(2);
        Stack<operations> operator = new Stack<>(1);
        stepData step_data = new stepData();
        
        String higherBuilder = "";
        
        String builder = "";
        
        String subExpression = "";
        boolean isInBracket = false;
        int bracketCounter = 0;
        
        for (int i = 0; i < expression.length(); i++){
            operations op1 = null;
            operations.TYPE operator_type = operations.TYPE.NONE;
            for(operations op : operations.values()){
                if(op.operator_type == operations.TYPE.POST){
                    if(op.evaluateSymbol(expression.charAt(i)) && (i == 0 || expression.charAt(i-1) != '1')){
                        op1 = op;
                        operator_type = operations.TYPE.POST;
                    }
                }else if(op.operator_type == operations.TYPE.PRE){
                    if(op.evaluateSymbol(expression.charAt(i)) && (i == expression.length()-1 || expression.charAt(i + 1) != '1')){
                        op1 = op;
                        operator_type = operations.TYPE.PRE;
                    }
                }
            }
            switch (operator_type) {
                case POST:
                    assert op1 != null;
                    higherBuilder += op1.neutral_value;
                    higherBuilder += op1.operator;
                    break;
                case PRE:
                    assert op1 != null;
                    higherBuilder += op1.operator;
                    higherBuilder += op1.neutral_value;
                    break;
                default:
                    higherBuilder += expression.charAt(i);
                    break;
            }
        }
        
        if((expression.length() > 1) && (expression.charAt(1) == '(' && 
                    (expression.charAt(0) == '-' || expression.charAt(0) == '+'))){
                char[] newBuilder = new char[higherBuilder.length()+1];
                for (int j = 0; j < newBuilder.length; j++){
                    if(j == 0)
                        newBuilder[j] = '0';
                    else{
                        newBuilder[j] = higherBuilder.charAt(j-1);
                    }
                }
                String str = "";
                for(char c : newBuilder)
                    str += c;
                
                higherBuilder = str;
        }
        
        expression = higherBuilder;
        
        for(int i = 0; i < expression.length(); i++){
            if (i == expression.length() - 1){
                for (operations op : operations.values()){
                    op.endFunction(expression.charAt(i), step_data);
                }
            }
            if (i == 0){
                if (expression.charAt(i) == '('){
                    isInBracket = true;
                    step_data.isOperator = false;
                }else{
                    for (operations op : operations.values()){
                        op.beginFunction(expression.charAt(i), step_data);
                    }
                }
            }else {
                boolean operatorFound = false;
                operations whichOperator = null;
                if(!isInBracket){
                    for(operations op : operations.values()){
                        if(op.evaluateSymbol(expression.charAt(i))){
                            if(step_data.isOperator){
                                if (expression.charAt(i) != '-' && expression.charAt(i) != '+')
                                    throw new IllegalStateException("the second conflicting symbol is illegal");
                                else{
                                    operatorFound = false;
                                }
                            }else{
                                operatorFound = true;
                                whichOperator = op;
                            }
                        }
                    }
                }else{
                    operatorFound = false;
                    whichOperator = null;
                }
                
                if (!operatorFound){
                    if(step_data.isOperator){
                        step_data.currentOperand = "";
                        
                        if(expression.charAt(i) == '('){
                            isInBracket = true;
                            step_data.isOperator = false;
                        }else{
                            step_data.increementDouble(String.valueOf(expression.charAt(i)));
                        }
                    }else {
                        if(isInBracket){
                            if (expression.charAt(i) == ')'){
                                if (bracketCounter == 0){
                                    isInBracket = false;
                                    step_data.setStepData(evaluate(subExpression, true));
                                    subExpression = "";
                                }else
                                    bracketCounter--;
                            }
                            if(expression.charAt(i) == '('){
                                bracketCounter++;
                            }
                        }
                        if(isInBracket){
                            subExpression += expression.charAt(i);
                        }else{
                            if(expression.charAt(i) != ')')
                                step_data.increementDouble(String.valueOf(expression.charAt(i)));
                        }
                    }
                }else {
                    double value = Double.parseDouble(step_data.currentOperand);
                    operand.push(value);
                    
                    if(operator.pullFirst() == null){
                        operator.push(whichOperator);
                        step_data.setStepData(whichOperator);
                    }else {
                        if(operations.ADD.evaluateSymbol(operator.pullFirst().operator) || 
                                operations.SUBTRACT.evaluateSymbol(operator.pullFirst().operator)){
                            builder += operand.pullFirst();
                            builder += operator.pullFirst().operator;
                            
                            operand.popFirst();
                            operator.popFirst();
                            
                            operator.push(whichOperator);
                            step_data.setStepData(whichOperator);
                            
                            continue;
                        }
                        
                        int precedence1 = operator.pullFirst().precedence;
                        assert whichOperator != null;
                        int precedence2 = whichOperator.precedence;
                            
                        if (precedence2 > precedence1){
                            builder += operand.pullFirst();
                            builder += operator.pullFirst().operator;
                            operand.popFirst();
                            operator.popFirst();
                            
                            operator.push(whichOperator);
                            step_data.setStepData(whichOperator);
                        }else {
                            double operatedValue = operator.pullFirst().function(operand.pull(0), operand.pull(1));
                            operand.popAll();
                            operand.push(operatedValue);
                            operator.popAll();
                            operator.push(whichOperator);
                            
                            step_data.setStepData(whichOperator);
                        }
                    }
                }
            }
        }
        operand.push(Double.parseDouble(step_data.currentOperand));
        
        if(operator.pullFirst() == null){
            return String.valueOf(operand.pullFirst());
        }
        
        boolean lastOperator = false;
        
        for(operations op : operations.values()){
            if (op != operations.ADD && op != operations.SUBTRACT){
                if (op.evaluateSymbol(operator.pullFirst().operator)){
                    lastOperator = true;
                }
            }
        }
        
        if(!lastOperator){
            builder += operand.pull(0);
            builder += operator.pullFirst().operator;
            builder += operand.pull(1);
        }else{
            double d = operator.pullFirst().function(operand.pull(0), operand.pull(1));
            if(builder.equals(""))
                return builder += d;
            builder += d;
        }
        
        operand.popAll();
        operator.popAll();
        step_data.currentOperand = "";
        step_data.currentOperation = null;
        step_data.isOperator = false;
        
        boolean containsComplexFunctions = false;
        for(int i = 0; i < builder.length(); i++){
            for(operations op : operations.values()){
                if (op != operations.ADD && op != operations.SUBTRACT){
                    if (op.evaluateSymbol(builder.charAt(i))){
                        containsComplexFunctions = true;
                    }
                }
            }
        }
        if (containsComplexFunctions){
            builder = evaluate(builder, false);
        }
        
        if (includeSimpleFunction){
            boolean containsSimpleFunctions= false;
            for(int i = 1; i < builder.length(); i++){
                for(operations op : operations.values()){
                    if (op == operations.ADD || op == operations.SUBTRACT){
                       if (op.evaluateSymbol(builder.charAt(i))){
                           containsSimpleFunctions = true;
                        }
                    }
                }
            }
            if (containsSimpleFunctions){
                String newBuilder = "";
                char previousChar = 'a';
                boolean operator_found = false;
                for(int i = 0; i < builder.length(); i++){
                    if (i == 0){
                        newBuilder += builder.charAt(i);
                    }else {
                        if((builder.charAt(i) == '+' || builder.charAt(i) == '-') && !operator_found){
                            operator_found = true;
                            previousChar = builder.charAt(i);
                            continue;
                        }
                        if (!operator_found){
                            newBuilder += builder.charAt(i);
                        }
                        if(operator_found){
                            if(builder.charAt(i) == '-' || builder.charAt(i) == '+'){
                                char c = symbolConflict(previousChar, builder.charAt(i)).operator;
                                newBuilder += c;
                            }else{
                                newBuilder += previousChar;
                                newBuilder += builder.charAt(i);
                            }
                            operator_found = false;
                        }
                    }
                }
                builder = newBuilder;
                builder = String.valueOf(simpleSolver(builder, operand, operator, step_data));
            }
        }
        
        return builder;
    }
    
    private static double simpleSolver(String expression, Stack<Double> operand,
            Stack<operations> operator, stepData step_data){
        
        for(int i = 0; i < expression.length(); i++){
            if (i == 0){
                for (operations op : operations.values()){
                    op.beginFunction(expression.charAt(i), step_data);
                }
            }else{
                boolean operatorFound = false;
                operations whichOperator = null;
                for(operations op : operations.values()){
                    if(op.evaluateSymbol(expression.charAt(i))){
                        operatorFound = true;
                        whichOperator = op;
                    }
                }
                
                if (!operatorFound){
                    if(step_data.isOperator){
                        step_data.currentOperand = "";
                        step_data.increementDouble(String.valueOf(expression.charAt(i)));
                    }else {
                        step_data.increementDouble(String.valueOf(expression.charAt(i)));
                    }
                }else {
                    double value = Double.parseDouble(step_data.currentOperand);
                    operand.push(value);
                    
                    if(operator.pullFirst() == null){
                        operator.push(whichOperator);
                        step_data.setStepData(whichOperator);
                    }else {
                        double operatedValue = operator.pullFirst().function(operand.pull(0), operand.pull(1));
                        operand.popAll();
                        operand.push(operatedValue);
                        operator.popAll();
                        operator.push(whichOperator);
                        
                        step_data.setStepData(whichOperator);
                    }
                }
            }
        }
        operand.push(Double.parseDouble(step_data.currentOperand));
        double d1 = operator.pullFirst().function(operand.pull(0), operand.pull(1));
        return d1;
    }
    
    private static operations symbolConflict(char c1, char c2){
            if((c1 == '+' && c2 == '+') || (c1 == '-' && c2 == '-'))
                return operations.ADD;
            else if((c1 == '+' && c2 == '-') || (c2 == '-' && c2 == '+'))
                return operations.SUBTRACT;
            throw new IllegalStateException("this condition isn't supposed to happen");
    }
    
    public static int findNextOperatorIndex(int begin, String expression){
        int nextIndex = -1;
        for (int j = begin; j < expression.length(); j++){
            for(operations op : operations.values()){
                if(op.evaluateSymbol(expression.charAt(j)))
                    nextIndex = j;
            }
        }
        return nextIndex;
    }
    
    static class stepData{
        public boolean isOperator = false;
        public operations currentOperation;
        public String currentOperand;
        
        public void setStepData(operations op){
            this.currentOperation = op;
            isOperator = true;
        }
        
        public void setStepData(String db){
            this.currentOperand = db;
            isOperator = false;
        }
        
        public void increementDouble(String db){
            this.currentOperand += db;
            this.isOperator = false;
        }
    }
    
    static class Stack<E> {
        
        private final Object[] e;
        private final int[] positionCounter;
        
        private final int size;
        
        public Stack(int size){
            e = (E[])new Object[size];
            positionCounter = new int[size];
            
            this.size = size;
            
            for(int i = 0; i < this.size; i++){
                positionCounter[i] = 0;
            }
        }
        
        public void push(E item){
            int freeIndex = -1;
            for (int i = 0; i < size; i++){
                if (positionCounter[i] == 0){
                    freeIndex = i;
                    positionCounter[i] = 1;
                    break;
                }
            }
            if (freeIndex == -1)
                throw new IllegalStateException("the Stack is full, cannot add any more value");
            e[freeIndex] = item;
        }
        
        public void push(E item, int index){
            e[index] = item;
            positionCounter[index] = 1;
        }
        
        @SuppressWarnings("unchecked")
        public E pull(int index){
            if (index > size - 1 || index < 0){
                throw new IllegalStateException("the provided index is beyond the Stack size");
            }
            return (E) e[index];
        }
        
        @SuppressWarnings("unchecked")
        public E pullFirst(){
            return pull(0);
        }
        
        public void popFirst(){
            pop(0);
        }
        
        public void pop(int index){
            if (index > size - 1 || index < 0){
                throw new IllegalStateException("the provided index is beyond the Stack size");
            }
            e[index] = null;
            positionCounter[index] = 0;
            for (int i = index; i < size - 1; i++){
                e[i] = e[i+1];
                e[i+1] = null;
                positionCounter[i] = positionCounter[i+1];
                positionCounter[i+1] = 0;
            }
        }
        
        public int getSize(){
           return this.size;
        }
        
        public void popAll(){
            for (int i = 0; i < size; i++){
                e[i] = null;
                positionCounter[i] = 0;
            }
        }
    }
}
