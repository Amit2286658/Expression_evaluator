import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Amit kumar.
 * execution on sololearn can take upto 30 ms more,
 * compared to execution on local machines.
 */


public class Main {

    private static Scanner scan = new Scanner(System.in);

    //when set to false, the entered angle will be considered to be in radians,
    //the angle values returned will also be in radians.
    //when set to true, the input or the output angle values will be in degrees.
    //use #setUseDegree to change
    private static boolean useDegree = true;

    public static void main(String[] args) {
        String expression = scan.nextLine();
        long startTime = System.nanoTime();
        System.out.println(Evaluate(expression));
        long endTime = System.nanoTime();
        System.out.println("Time taken for execution : "+(endTime-startTime)/1000000+" ms");
    }

    /*
    * function overloading is now supported,
    * overloaded functions uses the same operator, same name (look at the area of triangle function for example)
    * the difference comes in the type and amount of parameters they accept.

    * theoretically I don't understand how did it work, it just worked.
    * but on a side note, it works as predicted, i just don't understand my own codes anymore but whatever.*/

    /*volatile types are also supported,
    * so you may be wondering what the hell is a volatile type anyways?
    * well it's just the ability to change the type of the operator to something else on the fly,
    * just kidding, not exactly on the fly, since there are no ways to communicate with operators on the fly.
    * volatile types are predetermined and kind of expected types.

    * for example, the root operator works on both operands like this 2√4, 3√27, 4√256, etc,
      however there is a case where the left operand might be missing like this √4 or √9 or √16 etc,
      it's clear, we need default values in such cases and this is where the volatile types come in handy,
      we know it beforehand that in such cases the type will turn into a post type, therefore we can define
      2 as the default value(as one could guess, it indeed requires a default value, otherwise 1 will be used),
      and when such cases are encountered where one operand is missing, volatile types will come for rescue,
      and will automatically insert the default values in the place of the missing operand.
    */

    enum operations{
        // Updated it entirely, a new type, TYPE.CONSTANT is now supported, along with a whole suit of
        // trigonometric and hyperbolic functions, including inverse functions.
        // the bug which automatically added a multiplication symbol in front of the first operator
        // inside a bracket is solved now, try asin(sin(90)){is in degree} for example
        // or any other expression to test.

        /* EXISTING CHARACTER OPERATORS : S, C, T, I, O, P, J, K, M, Q, R, U, s, c, t, g, o, p, j, k,
         m, q, r, u, H, A, W, L, V, B, X, G, D, Z, Y, F, z, y, f, e, N, n, a.

        RESERVED CHARACTERS : E.

        * NOTE : Capital 'E' must never be used as an operator for any enum constant,
           as it's a character used by BigDecimal by default, using it is guaranteed
           to lock the program in an infinite loop.
        */
        /*
        * add a case in #function as needed and return the appropriate value for the custom
          operators.
        * the type defines which operand the operator will operate on, for operators which work on
          post operator
          values pass TYPE.POST, for operators which work on pre operator values pass TYPE.PRE, for operators
          which work on both operands there's no need to pass anything, as it's the default behaviour,
          the TYPE.CONSTANT is used for constant values like PI or natural logarithmic base,
          the TYPE.FUNCTION is used for functions, they don't require any precedence for they will
          be processed
          on top of every other operator, functions do not conflict with each other or other operators.
        * the function name is for convenience, like instead of using S30 or S(30) for sine function,
          with the name defined, you can use the name instead like sin is defined for sine function,
          which means now we can give input in this form sin(30) or sin30 or sin(-30) or sin-30.
        * function names, and their case is preserved but algorithm ignores their case anyways,
        * by passing true to the strictCase boolean variable, we can tell the algorithm to respect
        *  the case of the
          function names, they've been used to define EE and e, where "EE" has function name "E", while
          the natural log base has e as an operator, in this condition, the case will not be ignored.
          both capital and small 'e' will produce different result.
          NOTE : none of the above enum constants be it "EE" or natural log base has capital 'E' as
          an operator.
          The "EE" has 'G' as an operator while the natural log base has small 'e' as an operator.
        */

        ADD('+', 1),
        SUBTRACT('-', 1),
        MULTIPLY('*', 2, "x"),
        DIVIDE('/', 2, "÷"),
        EXPONENT('^', 1),
        SINE('S', 4, TYPE.POST, "sin"),
        COSINE('C', 4, TYPE.POST, "cos"),
        TANGENT('T', 4, TYPE.POST, "tan"),
        SECANT('I', 4, TYPE.POST, "sec"),
        COSECANT('O', 4, TYPE.POST, "csc"),
        COTANGENT('P', 4, TYPE.POST, "cot"),
        SINE_HYPERBOLA('J', 4, TYPE.POST, "sinh"),
        COSINE_HYPERBOLA('K', 4, TYPE.POST, "cosh"),
        TANGENT_HYPERBOLA('M', 4, TYPE.POST, "tanh"),
        SECANT_HYPERBOLA('Q', 4, TYPE.POST, "sech"),
        COSECANT_HYPERBOLA('R', 4, TYPE.POST, "csch"),
        COTANGENT_HYPERBOLA('U', 4, TYPE.POST, "coth"),
        SINE_INVERSE('s', 4, TYPE.POST, "asin"),
        COSINE_INVERSE('c', 4, TYPE.POST, "acos"),
        TANGENT_INVERSE('t', 4, TYPE.POST, "atan"),
        SECANT_INVERSE('g', 4, TYPE.POST, "asec"),
        COSECANT_INVERSE('o', 4, TYPE.POST, "acsc"),
        COTANGENT_INVERSE('p', 4, TYPE.POST, "acot"),
        SINE_HYPERBOLA_INVERSE('j', 4, TYPE.POST, "asinh"),
        COSINE_HYPERBOLA_INVERSE('k', 4, TYPE.POST, "acosh"),
        TANGENT_HYPERBOLA_INVERSE('m', 4, TYPE.POST, "atanh"),
        SECANT_HYPERBOLA_INVERSE('q', 4, TYPE.POST, "asech"),
        COSECANT_HYPERBOLA_INVERSE('r', 4, TYPE.POST, "acsch"),
        COTANGENT_HYPERBOLA_INVERSE('u', 4, TYPE.POST, "acoth"),
        EE('G', 5, "E", true),
        RAND('D', 6, TYPE.CONSTANT, "random"),
        e('e', 6, TYPE.CONSTANT, "euler", true),
        PI('π', 6, TYPE.CONSTANT, "pi"),
        MODULUS('%', 3),
        FACTORIAL('!', 5, TYPE.PRE),
        HYPOTENUSE('H', TYPE.FUNCTION, "Hypotenuse", 2),
        ROOT('√', 5, 2, TYPE.POST),
        SUMMATION('B', TYPE.FUNCTION, "sum", 3, new int[]{
                operations.ARGUMENT_DOUBLE,
                operations.ARGUMENT_DOUBLE,
                operations.ARGUMENT_STRING
        }),
        ADD_ALL('A', TYPE.FUNCTION, "Add"),
        LOG_10('W', 5, TYPE.POST, "log10"),
        LOG_E('L', 5, TYPE.POST, "ln"),
        LOG_D('V', 5, "log"),
        AREA_OF_RECT('Z', TYPE.FUNCTION, "area[□]", 2),
        AREA_OF_CIRCLE('Y', TYPE.FUNCTION, "area[○]", 1),
        AREA_OF_TRIANGLE('F', TYPE.FUNCTION, "area[Δ]", 2, 1),
        HERONS_FORMULA('F', TYPE.FUNCTION, "area[Δ]", 3, 2),
        PERIMETER_OF_RECT('z', TYPE.FUNCTION, "peri[□]", 2),
        PERIMETER_OF_CIRCLE('y', TYPE.FUNCTION, "peri[○]", 1),
        PERIMETER_OF_TRIANGLE('f', TYPE.FUNCTION, "peri[Δ]", 3),
        HCF('N', TYPE.FUNCTION, "HCF"),
        LCM('n', TYPE.FUNCTION, "LCM"),
        DETERMINANT('a', TYPE.FUNCTION, "Determinant");

        /*d1 and d2 are the BigDecimal values that are just before and after the operator.
          either d1 or d2 would be just '1' if the operator is either TYPE.POST_TYPE or
          TYPE.PRE_TYPE respectively, values from d1 or d2 can be ignored in such case
          or just be multiplied since, they would be just '1', they will have no effect on the result.
        * d1 and d2 both would be 1 if the type is #TYPE.CONSTANT, and can be safely ignored.
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
                    return Math.sin(getAngle(d2, false));
                case 'C' :
                    return Math.cos(getAngle(d2, false));
                case 'T' :
                    return Math.tan(getAngle(d2, false));
                case '!' :
                    return factorial((int)d1);
                case '√' :
                    if (d2 < 0 && d1 % 2 != 0)
                        return Math.pow(Math.abs(d2), 1/d1) * -1;
                    return Math.pow(d2, 1/d1);
                case '%' :
                    return d1 % d2;
                case 'V' :
                    return Math.log(d2)/Math.log(d1);
                case 'W' :
                    return Math.log10(d2);
                case 'L' :
                    return Math.log(d2);
                case 'I' :
                    return 1/Math.cos(getAngle(d2, false));
                case 'O' :
                    return 1/Math.sin(getAngle(d2, false));
                case 'P' :
                    return 1/Math.tan(getAngle(d2, false));
                case 'J' :
                    return Math.sinh(d2);
                case 'K' :
                    return Math.cosh(d2);
                case 'M' :
                    return Math.tanh(d2);
                case 'Q' :
                    return 1/Math.cosh(d2);
                case 'R' :
                    return 1/Math.sinh(d2);
                case 'U' :
                    return 1/Math.tanh(d2);
                case 's' :
                    return getAngle(Math.asin(d2), true);
                case 'c' :
                    return getAngle(Math.acos(d2), true);
                case 't' :
                    return getAngle(Math.atan(d2), true);
                case 'i' :
                    return getAngle(Math.acos(1/d2), true);
                case 'o' :
                    return getAngle(Math.asin(1/d2), true);
                case 'p' :
                    return getAngle(Math.atan(1/d2), true);
                case 'j' :
                    return Math.log(d2 +
                            Math.sqrt(Math.pow(d2, 2) + 1));
                case 'k' :
                    return Math.log(d2 +
                            Math.sqrt(Math.pow(d2, 2) - 1));
                case 'm' :
                    return (1.0/2.0)*Math.log((1 + d2)/
                            (1 - d2));
                case 'q' :
                    return Math.log((1 + Math.sqrt(1 - Math.pow(d2, 2)))/
                            d2);
                case 'r' :
                    return Math.log((1/d2) +
                            ((Math.sqrt(1 + Math.pow(d2, 2)))/Math.abs(d2)));
                case 'u' :
                    return (1.0/2.0)*Math.log((d2 + 1) /
                            (d2 - 1));
                case 'G' :
                    return d1 * Math.pow(10, d2);
                case 'D' :
                    return getRandom();
                case 'π' :
                    return Math.PI;
                case 'e' :
                    return (Math.E);
            }
            return 0;
        }

        /*
        * this function will be called when the type is TYPE.FUNCTION,
          it gives all the values seperated by comma inside the parentheses,
          parentheses is important to encapsulate the arguments of the function.

        * identity is the id of the functions, ignore if there are no overloads.
        */
        public double function(argument[] arguments, int identity){
            switch(operator){
                case 'H' :
                    return Math.sqrt(Math.pow(arguments[0].double_value, 2) + Math.pow(arguments[1].double_value, 2));
                case 'B' :
                    return summation((int)arguments[0].double_value,
                            (int)arguments[1].double_value,
                            arguments[2].string_value);
                case 'A' :
                    double d = 0;
                    for(argument ar : arguments){
                        d = d + ar.double_value;
                    }
                    return d;
                case 'Z' :
                    return arguments[0].double_value *
                            arguments[1].double_value;
                case 'Y' :
                    return Math.PI * Math.pow(arguments[0].double_value, 2);
                case 'F' :
                    switch (identity){
                        case 1 :
                            return 1.0/2.0 * arguments[0].double_value *
                                    arguments[1].double_value;
                        case 2 :
                            double a = arguments[0].double_value, b = arguments[1].double_value,
                                    c = arguments[2].double_value;
                            double smp = a + b + c;
                            smp = smp/2;

                            return Math.sqrt(smp * (smp - a) * (smp - b) * (smp-c));
                    }
                    return 0;
                case 'z' :
                    return 2*(arguments[0].double_value +
                            arguments[1].double_value);
                case 'y' :
                    return 2 * Math.PI * arguments[0].double_value;
                case 'f' :
                    return arguments[0].double_value +
                            arguments[1].double_value +
                            arguments[2].double_value;
                case 'N' :
                    double[] values = new double[arguments.length];
                    for (int i = 0; i < arguments.length; i++){
                        values[i] = arguments[i].double_value;
                    }
                    return HCF(values);
                case 'n' :
                    double[] values1 = new double[arguments.length];
                    for (int i = 0; i < arguments.length; i++){
                        values1[i] = arguments[i].double_value;
                    }
                    return LCM(values1);
                case 'a' :
                    double[] values2 = new double[arguments.length];
                    for (int i = 0; i < arguments.length; i++){
                        values2[i] = arguments[i].double_value;
                    }
                    return determinant(values2);
                case 'b' :
                    return 1;
            }
            return 0;
        }

        //that was all for the custom operator,
        //the codes below and everything else, does the rest of the work.
        public enum TYPE {
            PRE, POST, NONE, FUNCTION, CONSTANT
        }

        public static final int ARGUMENT_DOUBLE = 1;
        public static final int ARGUMENT_STRING = 2;

        public final char operator;
        public final int precedence;
        public int neutral_value = 1;

        public TYPE volatile_type = null;
        public boolean isVolatile = false;

        public int function_identity = 1;

        public TYPE operator_type = operations.TYPE.NONE;
        public String functionName = null;
        public int argumentSize = -1;
        public int[] argument_type = {
                ARGUMENT_DOUBLE
        };
        public boolean strictCase = false;

        operations(char operator, int precedence){
            this.operator = operator;
            this.precedence= precedence;
        }

        operations(char operator, int precedence, int neutral_value, TYPE volatile_type){
            this(operator, precedence);
            this.neutral_value = neutral_value;
            this.isVolatile = true;
            this.volatile_type = volatile_type;
        }

        operations(char operator, int precedence, TYPE type, boolean strictCase){
            this(operator, precedence);
            this.operator_type = type;
            this.strictCase = strictCase;
        }

        operations(char operator, int precedence, TYPE type){
            this(operator, precedence);
            this.operator_type = type;
        }

        operations(char operator, int precedence, TYPE type, String functionName, boolean strictCase){
            this(operator, precedence, type, strictCase);
            this.functionName = functionName;
        }

        operations(char operator, int precedence, TYPE type, String functionName){
            this(operator, precedence, type, false);
            this.functionName = functionName;
        }

        operations(char operator, int precedence, String functionName, boolean strictCase){
            this(operator, precedence, TYPE.NONE, strictCase);
            this.functionName = functionName;
        }

        operations(char operator, int precedence, String functionName){
            this(operator, precedence, TYPE.NONE, false);
            this.functionName = functionName;
        }

        operations(char operator, int precedence, String functionName, int neutral_value, TYPE volatile_type){
            this(operator, precedence, functionName);
            this.neutral_value = neutral_value;
            this.isVolatile = true;
            this.volatile_type = volatile_type;
        }

        operations(char operator, TYPE type, String functionName, boolean strictCase){
            this(operator, 0, type, functionName, strictCase);
        }

        operations(char operator, TYPE type, String functionName){
            this(operator, 0, type, functionName,
                    false);
        }

        operations(char operator, TYPE type, String functionName, boolean strictCase, int argumentSize){
            this(operator, 0, type, functionName, strictCase);
            this.argumentSize = argumentSize;
        }

        operations(char operator, TYPE type, String functionName, int argumentSize){
            this(operator, 0, type, functionName, false);
            this.argumentSize = argumentSize;
        }

        operations(char operator, TYPE type, String functionName, int argumentSize, int function_identity){
            this(operator, type, functionName, argumentSize);
            this.function_identity = function_identity;
        }

        operations(char operator, TYPE type, String functionName, boolean strictCase, int argumentSize,
                   int[] argumentType){
            this(operator, type, functionName, strictCase, argumentSize);
            this.argument_type = argumentType;
        }

        operations(char operator, TYPE type, String functionName, boolean strictCase, int argumentSize,
                   int[] argumentType, int function_identity){
            this(operator, type, functionName, strictCase, argumentSize, argumentType);
            this.function_identity = function_identity;
        }

        operations(char operator, TYPE type, String functionName, int argumentSize, int[] argumentType){
            this(operator, type, functionName, false, argumentSize);
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

        public int getId(){
            return function_identity;
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

        /*
        * integer at the first index represents the argument size of the function
        * integer at the second index represents the function identity
        * integer at the third index represents the length of the argument types
        * integers from the fourth index and onward represents the types of the arguments.
        */
        public int[] getFunctionArguments(char operator){
            int[] int_args_arr = new int[argumentSize == -1 ? 4 :
                    argument_type.length == argumentSize ? argumentSize + 3 : 4];
            if (isFunction(operator)){
                int_args_arr[0] = argumentSize;
                int_args_arr[1] = function_identity;
                int_args_arr[2] = argument_type.length;
                if (argumentSize != -1){
                    if (argument_type.length > 1){
                        System.arraycopy(argument_type, 0, int_args_arr,
                                3, argument_type.length);
                    }else {
                        int_args_arr[3] = operations.ARGUMENT_DOUBLE;
                    }
                }else {
                    int_args_arr[3] = operations.ARGUMENT_DOUBLE;
                }
                return int_args_arr;
            }else {
                throw new UnsupportedOperationException("Bad call : #getFunctionArguments should only be" +
                        " called for functions and not for operators that work on operands.");
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
        double number = 1;
        while (factor != 1){
            number *= factor--;
        }
        return number;
    }

    public static double summation(int initialBound, int finalBound, String expression){
        double exp = 0;
        char variable_char = 'a';
        for(char c : expression.toCharArray()){
            if(Character.isLetter(c)){
                variable_char = c;
                break;
            }
        }
        for(int i = initialBound; i <= finalBound; i++){
            exp += Double.parseDouble(Evaluate(expression.replaceAll(
                    String.valueOf(variable_char), String.valueOf(i))));
        }
        return exp;
    }

    public static double HCF(double... values){
        if (values.length == 1)
            return values[0];
        else if (values.length == 0)
            throw new NullPointerException("input array is empty." +
                    " provide an input with a valid size");
        else{
            double var1 = Math.min(values[0], values[1]);
            double var2 = Math.max(values[0], values[1]);
            double remainder = var2 % var1;
            while (remainder != 0){
                var2 = var1;
                var1 = remainder;
                remainder = var2 % var1;
            }
            double[] newValues = new double[values.length - 1];
            newValues[0] = var1;
            for (int i = 0; i < values.length; i++){
                if (i != 0 && i != 1)
                    newValues[i - 1] = values[i];
            }
            return HCF(newValues);
        }
    }

    public static double LCM(double... values){
        int[] newValues = new int[values.length];
        for (int i = 0; i < values.length; i++){
            newValues[i] = (int) values[i];
        }
        return LCM(2, 1, newValues);
    }

    private static int LCM(int startingDivisor, int multiplier, int[] values){
        if (values.length == 0)
            throw new NullPointerException("empty values array in LCM");

        boolean atLeastOneDividend = false;

        for (int i = 0; i < values.length; i++){
            if (values[i] != 1){
                if (values[i] % startingDivisor == 0){
                    atLeastOneDividend = true;
                    values[i] = values[i] / startingDivisor;
                }
            }
        }

        if (atLeastOneDividend){
            multiplier *= startingDivisor;
            multiplier = LCM(startingDivisor, multiplier, values);
        }else {
            boolean numberRemaining = false;
            for (int value : values){
                if (value != 1){
                    numberRemaining = true;
                    break;
                }
            }
            if (numberRemaining) {
                switch (startingDivisor) {
                    case 2:
                        multiplier = LCM(3, multiplier, values);
                        break;
                    case 3:
                        multiplier = LCM(5, multiplier, values);
                        break;
                    case 5:
                        multiplier = LCM(7, multiplier, values);
                        break;
                    default:
                        int newDivisor = 0;
                        for (int value : values){
                            if (value != 1) {
                                newDivisor = value;
                                break;
                            }
                        }
                        multiplier = LCM(newDivisor, multiplier, values);
                }
            }
        }
        return multiplier;
    }

    public static double getRandom(){
        return Math.random();
        //given by a fairly rolled dice.
        //return BigDecimal.valueOf(4);
    }

    public static double getAngle(double angle, boolean reverse){
        if(useDegree){
            if (reverse)
                return Math.toDegrees(angle);
            else
                return Math.toRadians(angle);
        }else
            return angle;
    }

    public static void setUseDegree(boolean shouldUseDegree) {
        useDegree = shouldUseDegree;
    }

    public static double[] systemOfEquations(String ListOfEquations){
        String[] equations = ListOfEquations.split(",");

        double[][] matrixD = new double[equations.length][equations.length];
        double[] equators = new double[equations.length];

        double determinantD = 0;

        for (int k = 0; k < equations.length; k++){
            String equation = equations[k].replaceAll("\\s","");

            String[] handSide = equation.split("=");

            String[] terms = handSide[0].split("(?=[-+])");

            for (int i = 0; i < terms.length; i++){

                char c = 0;

                for (int j = 0; j < terms[i].toCharArray().length; j++){
                    if (Character.isLetter(terms[i].charAt(j))){
                        c = terms[i].charAt(j);
                        break;
                    }
                }
                terms[i] = terms[i].replace(String.valueOf(c), "");

                matrixD[k][i] = Double.parseDouble(terms[i]);
            }

            equators[k] = Double.parseDouble(handSide[1]);
        }

        determinantD = determinant(matrixD);

        if (determinantD == 0){
            throw new IllegalStateException("the given system of equations is not linear");
        }

        double[] finalValues = new double[equations.length];

        int subMatrixVariablePositionCounter = 0;

        for (int i = 0; i < equations.length; i++){
            double[][] subMatrix = new double[matrixD.length][matrixD.length];

            for (int n = 0; n < subMatrix.length; n++){
                System.arraycopy(matrixD[n], 0, subMatrix[n], 0, subMatrix[i].length);
            }

            for (int j = 0; j < subMatrix[i].length; j++){
                subMatrix[j][subMatrixVariablePositionCounter] = equators[j];
            }

            finalValues[i] = determinant(subMatrix)/determinantD;

            subMatrixVariablePositionCounter++;
        }
        return finalValues;
    }

    public static double determinant(double... values){
        double a = Math.sqrt(values.length);
        if (a - Math.floor(a) != 0)
            throw new IllegalArgumentException("the length of the matrix must be a perfect square" +
                    " in order to create a square matrix");

        int a1 = (int)a;

        double[][] matrix = new double[a1][a1];

        int columnCounter = 0;
        int rowCounter = 0;

        for (double value : values) {
            matrix[rowCounter][columnCounter] = value;
            columnCounter++;

            if (columnCounter == a1) {
                columnCounter = 0;
                rowCounter++;
            }
        }

        return determinant(matrix);
    }

    private static double determinant(double[][] values){
        for (double[] value : values) {
            if (value.length != values.length)
                throw new IllegalStateException("the given matrix is not in square form");
        }

        if (values.length == 2){
            return values[0][0] * values[1][1] - values[1][0] * values[0][1];
        }

        double finalValue = 0;

        boolean offsetPositions = false;

        for (int i = 0; i < values.length; i++){
            double coefficient = values[i][0];

            double[][] matrix = new double[values.length - 1][values.length - 1];

            for (int j = 0; j < values.length; j++) {
                if (j == i) {
                    offsetPositions = true;
                    continue;
                }

                for (int k = 1; k < values.length; k++) {
                    if (offsetPositions) {
                        matrix[j - 1][k - 1] = values[j][k];
                    } else {
                        matrix[j][k - 1] = values[j][k];
                    }
                }
            }

            coefficient *= determinant(matrix);
            offsetPositions = false;

            if (i % 2 == 0){
                finalValue += coefficient;
            }else {
                finalValue -= coefficient;
            }
        }
        return finalValue;
    }

    public double getRationalisedDoubleValue(double value){

        return 0;
    }

    //end of default functions

    public static String Evaluate(String expression){
        if (expression.isEmpty()){
            throw new NullPointerException("the given string is empty, provide a valid string, " +
                    "or run a regression test");
        }

        expression = expression.trim();
        expression = expression.replaceAll("\\s+", "");
        expression = expression.replaceAll("\\)\\(", "\\)*\\(");

        String functionList = "";
        for(operations op : operations.values()){
            if(op.functionName != null){
                functionList += (functionList.length() != 0 ? "?" : "")
                        +op.functionName+"."+op.operator+"."+(op.strictCase ? "1" : "0");
            }
        }

        if(!functionList.isEmpty()){
            if(functionList.contains("?")){
                String[] names = functionList.split("\\?");
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

                //function name replacement with their respective operators happens here.
                for(String str : names){
                    String[] args = str.split("\\.");
                    if (args[2].equals("1")){
                        expression = expression.replaceAll(args[0], args[1]);
                    }else {
                        expression = expression.replaceAll("(?i)" + Pattern.quote(args[0]), args[1]);
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
                    (String.valueOf(expression.charAt(i-1))).matches("[1234567890]")){
                str += '*';
                str += expression.charAt(i);
            }else {
                if(i != expression.length()-1 && expression.charAt(i) == ')' &&
                        String.valueOf(expression.charAt(i + 1)).matches("[1234567890]")){
                    str += expression.charAt(i);
                    str += '*';
                }else if(i != expression.length() - 1 && expression.charAt(i) == ')' &&
                        functionOperators.length() != 0 &&
                        String.valueOf(expression.charAt(i + 1)).matches(functionOperators)){
                    str += expression.charAt(i);
                    str += '*';
                }else {
                    str += expression.charAt(i);
                }
            }
        }

        expression = str;

        String builder = "";
        int step_pos = -2;
        for (int i = 0; i < expression.length(); i++){
            boolean isConstant = false;
            for (operations op : operations.values()){
                if (op.operator_type == operations.TYPE.CONSTANT){
                    if (op.evaluateSymbol(expression.charAt(i))){
                        isConstant = true;
                        if (step_pos != i - 1) {
                            step_pos = i;
                        }
                    }
                }
            }
            if (isConstant && i == 0){
                builder += expression.charAt(i);
            }
            else if (isConstant){
                if (step_pos == i - 1){
                    builder += '*';
                    builder += expression.charAt(i);
                    step_pos = i;
                }else {
                    builder += expression.charAt(i);
                }
            }
            else {
                builder += expression.charAt(i);
            }
        }

        expression = builder;

        expression = evaluateFunction(expression);

        boolean containsFunction = false;
        for(operations op : operations.values()){
            if(op.operator_type == operations.TYPE.FUNCTION){
                containsFunction = expression.contains(op.functionName);
            }
        }
        if(containsFunction)
            expression = Evaluate(expression);

        /*the codes below does extensive bracket insertion, the code is repetitive, and most of the work could be done
        * in one or two loops, however, it will be a mess to combine all this logic, therefore I'm doing bracket insertion
        * for each different case separately to retain my sanity.*/

        boolean lastCharOperator = false, numFound = false;
        int bracketCounter = 0;
        String tempSubExpression = "";
        for (int i = 0; i < expression.length(); i++){
            for (operations op : operations.values()){
                if (op.evaluateSymbol(expression.charAt(i))){
                    if ((op.isVolatile && op.volatile_type == operations.TYPE.POST) ||
                    op.operator_type == operations.TYPE.POST){
                        if (!lastCharOperator){
                            lastCharOperator = true;
                        } else{
                            tempSubExpression += "(";
                            bracketCounter++;
                        }
                    } else {
                        if (numFound){
                            while (bracketCounter > 0){
                                tempSubExpression += ")";
                                bracketCounter--;
                            }
                            numFound = false;
                        }
                    }
                }
            }
            boolean matches = String.valueOf(expression.charAt(i)).matches("[1234567890]");

            if (matches){
                if (lastCharOperator)
                    lastCharOperator = false;
                numFound = true;
            }
            tempSubExpression += expression.charAt(i);

            if (bracketCounter != 0 && i == expression.length() - 1){
                for (int k = 0; k < bracketCounter; k++){
                    tempSubExpression += ")";
                }
            }
        }
        expression = tempSubExpression;

        tempSubExpression = "";
        lastCharOperator = false;
        numFound = false;
        bracketCounter = 0;

        for (int i = expression.length() - 1; i >= 0; i--){
            for (operations op : operations.values()){
                //3√√64--5+3!!/2
                if (op.evaluateSymbol(expression.charAt(i))){
                    if ((op.isVolatile && op.volatile_type == operations.TYPE.PRE) ||
                    op.operator_type == operations.TYPE.PRE) {
                        if (!lastCharOperator){
                            lastCharOperator = true;
                        } else{
                            tempSubExpression += ")";
                            bracketCounter++;
                        }
                    } else {
                        if (numFound){
                            while (bracketCounter > 0){
                                tempSubExpression += "(";
                                bracketCounter--;
                            }
                            numFound = false;
                        }
                    }
                }
            }
            boolean matches = String.valueOf(expression.charAt(i)).matches("[1234567890]");

            if (matches){
                if (lastCharOperator)
                    lastCharOperator = false;
                numFound = true;
            }
            tempSubExpression += expression.charAt(i);

            if (bracketCounter != 0 && i == 0){
                for (int k = 0; k < bracketCounter; k++){
                    tempSubExpression += "(";
                }
            }
        }

        String reverseTempSubString = "";
        for (int i = tempSubExpression.length() - 1; i >= 0; i--){
            reverseTempSubString += tempSubExpression.charAt(i);
        }

        expression = reverseTempSubString;

        tempSubExpression = "";
        lastCharOperator = false;
        bracketCounter = 0;

        for (int i = 0; i < expression.length(); i++){
            for (operations op : operations.values()){
                if (op.evaluateSymbol(expression.charAt(i))){
                    if (op == operations.ADD || op == operations.SUBTRACT){
                        if (!lastCharOperator)
                            lastCharOperator = true;
                        else {
                            tempSubExpression += "(";
                            bracketCounter++;
                        }
                    }else {
                        if (lastCharOperator)
                            lastCharOperator = false;
                    }
                }
            }
            boolean match = String.valueOf(expression.charAt(i)).matches("[1234567890]");

            if (match){
                if (lastCharOperator)
                    lastCharOperator = false;
            }

            tempSubExpression += expression.charAt(i);

            if (bracketCounter != 0 && i == expression.length() - 1){
                while (bracketCounter > 0){
                    tempSubExpression += ")";
                    bracketCounter--;
                }
            }
        }
        expression = tempSubExpression;

        return evaluateOperation(expression, true);
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
                            if(i != 0 && String.valueOf(expression.charAt(i-1)).
                                    matches("[1234567890]")){
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
                if(!isInBracket){
                    String[] array = subString.split(",");
                    operations.argument[] arguments = new operations.argument[array.length];

                    int function_identity = -1;

                    ///////////////////////////////////////////////////////////////////////////////////////
                    int[] args_array = new int[array.length];

                    for (int j = 0; j < array.length; j++){
                        try {
                            double arg1 = Double.parseDouble(array[j]);
                            arguments[j] = new operations.argument(arg1, null,
                                    operations.ARGUMENT_DOUBLE);
                            args_array[j] = operations.ARGUMENT_DOUBLE;
                        }catch (NumberFormatException e){
                            arguments[j] = new operations.argument(0, array[j],
                                    operations.ARGUMENT_STRING);
                            args_array[j] = operations.ARGUMENT_STRING;
                        }
                    }

                    boolean matched = false;

                    for (operations op : operations.values()){
                        if (op.operator_type == operations.TYPE.FUNCTION &&
                                op.evaluateSymbol(whichOperator.operator)){
                            int[] functionArgs = op.getFunctionArguments(whichOperator.operator);
                            boolean match_found = true;

                            if (functionArgs[0] == -1){
                                for (int k : args_array){
                                    if (k == operations.ARGUMENT_STRING) {
                                        match_found = false;
                                        break;
                                    }
                                }
                            }else {
                                if (functionArgs[0] != args_array.length) {
                                    match_found = false;
                                }else {
                                    if (functionArgs[2] == 1){
                                        if (functionArgs[3] == operations.ARGUMENT_DOUBLE){
                                            for (int value : args_array) {
                                                if (value != operations.ARGUMENT_DOUBLE) {
                                                    match_found = false;
                                                    break;
                                                }
                                            }
                                        } else if (functionArgs[3] == operations.ARGUMENT_STRING){
                                            if (args_array.length > 1)
                                                throw new IllegalArgumentException("functions accepting " +
                                                        "strings as a parameter cannot define string as a default" +
                                                        "parameter type for multiple parameters");
                                            else
                                                if (args_array[0] != operations.ARGUMENT_STRING)
                                                    match_found = false;
                                        }
                                    }else {
                                        for (int k = 0; k < args_array.length; k++){
                                            if (functionArgs[k + 3] != args_array[k]) {
                                                match_found = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (match_found){
                                function_identity = functionArgs[1];
                                matched = true;
                                break;
                            }
                        }
                    }

                    if (!matched)
                        throw new IllegalArgumentException("invalid arguments");

                    ////////////////////////////////////////////////////////////////////////////////////////
                    /*if(whichOperator.argumentSize != -1 &&
                            whichOperator.argumentSize != arguments.length){
                        throw new IllegalStateException("the provided argument length does"
                                + " not match the required length of the argument");
                    }
                    for (int j = 0; j < array.length; j++) {
                        if(whichOperator.argumentSize == -1){
                            arguments[j] = new operations.argument(
                                    Double.parseDouble(Evaluate(array[j])),
                                    "", operations.ARGUMENT_DOUBLE);
                        }else{
                            if(whichOperator.argument_type.length > 1){
                                switch (whichOperator.argument_type[j]) {
                                    case operations.ARGUMENT_DOUBLE:
                                        arguments[j] = new operations.argument(
                                                Double.parseDouble(Evaluate(array[j])),
                                                "", operations.ARGUMENT_DOUBLE);
                                        break;
                                    case operations.ARGUMENT_STRING:
                                        arguments[j] = new operations.argument(0, array[j],
                                                operations.ARGUMENT_STRING);
                                        break;
                                    default:
                                        throw new IllegalStateException("the given argument type is illegal");
                                }
                            }else{
                                arguments[j] = new operations.argument(
                                        Double.parseDouble(Evaluate(array[j])),
                                        "", operations.ARGUMENT_DOUBLE);
                            }
                        }
                    }*/
                    double d = whichOperator.function(arguments, function_identity);
                    str += d;
                    isInFunction = false;
                }
            }else{
                str += expression.charAt(i);
            }
        }
        return str;
    }

    public static String evaluateOperation(String expression, boolean includeSimpleFunction){
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
                    if(op.evaluateSymbol(expression.charAt(i))){
                        op1 = op;
                        operator_type = operations.TYPE.POST;
                    }
                }else if(op.operator_type == operations.TYPE.PRE){
                    if(op.evaluateSymbol(expression.charAt(i))){
                        op1 = op;
                        operator_type = operations.TYPE.PRE;
                    }
                }else if(op.operator_type == operations.TYPE.CONSTANT){
                    if(op.evaluateSymbol(expression.charAt(i))){
                        op1 = op;
                        operator_type = operations.TYPE.CONSTANT;
                    }
                }else if (op.operator_type == operations.TYPE.NONE){
                    if (op.evaluateSymbol(expression.charAt(i))) {
                        op1 = op;
                        operator_type = operations.TYPE.NONE;
                    }
                }
            }

            switch (operator_type) {
                case NONE:
                    if (op1 != null && op1.isVolatile){
                        if (op1.volatile_type == operations.TYPE.POST) {
                            if (i != 0 && expression.charAt(i - 1) != '(') {
                                if (!String.valueOf(expression.charAt(i - 1)).matches("[1234567890]")) {
                                    higherBuilder += op1.neutral_value;
                                }
                            } else {
                                higherBuilder += op1.neutral_value;
                            }
                            higherBuilder += op1.operator;
                        }else if (op1.volatile_type == operations.TYPE.PRE){
                            higherBuilder += op1.operator;
                            if (i != expression.length() - 1 && expression.charAt(i + 1) != ')') {
                                if (!String.valueOf(expression.charAt(i + 1)).matches("[1234567890]")){
                                    higherBuilder += op1.neutral_value;
                                }
                            } else {
                                higherBuilder += op1.neutral_value;
                            }
                        }
                    }else
                        higherBuilder += expression.charAt(i);
                    break;
                case POST:
                    if (i != 0 && expression.charAt(i - 1) != '('){
                        if (String.valueOf(expression.charAt(i - 1)).matches("[1234567890]"))
                            higherBuilder += "*";
                    }
                    higherBuilder += op1.neutral_value;
                    higherBuilder += op1.operator;
                    break;
                case PRE:
                    higherBuilder += op1.operator;
                    higherBuilder += op1.neutral_value;

                    if (i != expression.length() - 1 && expression.charAt(i + 1) != ')') {
                        if (String.valueOf(expression.charAt(i + 1)).matches("[1234567890]"))
                            higherBuilder += "*";
                    }
                    break;
                case CONSTANT:
                    if (i != 0 && String.valueOf(expression.charAt(i-1)).matches("[1234567890]")) {
                        higherBuilder += '*';
                    }
                    higherBuilder += op1.neutral_value;
                    higherBuilder += op1.operator;
                    higherBuilder += op1.neutral_value;
                    if (i != expression.length() - 1 && String.valueOf(expression.charAt(i+1)).
                            matches("[1234567890]")) {
                        higherBuilder += '*';
                    }
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
                                if (expression.charAt(i) != '-' && expression.charAt(i) != '+') {
                                    throw new IllegalStateException
                                            ("the second symbol is conflicting");
                                }
                                else{
                                    operatorFound = false;
                                }
                            }else{
                                if(expression.charAt(i - 1) == 'E'){
                                    operatorFound = false;
                                }else {
                                    operatorFound = true;
                                    whichOperator = op;
                                }
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
                                    step_data.setStepData(evaluateOperation(subExpression, true));
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

                    if (operator.pullFirst() != null) {
                        if (operations.ADD.evaluateSymbol(operator.pullFirst().operator) ||
                                operations.SUBTRACT.evaluateSymbol(operator.pullFirst().operator)) {
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

                        if (precedence2 > precedence1) {
                            builder += operand.pullFirst();
                            builder += operator.pullFirst().operator;
                            operand.popFirst();
                            operator.popFirst();

                        } else {
                            double operatedValue = operator.pullFirst().function(operand.pull(0),
                                    operand.pull(1));
                            operand.popAll();
                            operand.push(operatedValue);
                            operator.popAll();

                        }
                    }
                    operator.push(whichOperator);
                    step_data.setStepData(whichOperator);
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
            double d = operator.pullFirst().function(operand.pull(0),
                    operand.pull(1));
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
            builder = evaluateOperation(builder, false);
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
                        if(expression.charAt(i - 1) == 'E'){
                            operatorFound = false;
                        }else {
                            operatorFound = true;
                            whichOperator = op;
                        }
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
                        double operatedValue = operator.pullFirst().function(operand.pull(0),
                                operand.pull(1));
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
        if (operator.isEmpty()){
            return operand.pullFirst();
        }
        double d1 = operator.pullFirst().function(operand.pull(0),
                operand.pull(1));
        return d1;
    }

    private static operations symbolConflict(char c1, char c2){
        if((c1 == '+' && c2 == '+') || (c1 == '-' && c2 == '-'))
            return operations.ADD;
        else if((c1 == '+' && c2 == '-') || (c1 == '-' && c2 == '+'))
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
            e = new Object[size];
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

        public boolean isEmpty(){
            for(int i = 0; i < size; i++){
                if (e[i] != null) {
                    return false;
                }
            }
            return true;
        }
    }
}

