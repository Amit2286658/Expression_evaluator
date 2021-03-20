import java.math.BigDecimal;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Amit kumar.
 * execution on sololearn can take upto 30 ms more,
 * compared to execution on local machines.
 */

/*
 * UPDATE : I am making an android calculator based on this Algorithm.
 * :)
*/
public class ExpressionEvaluator {

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

    enum operations{
        // Updated it entirely, a new type, TYPE.CONSTANT is now supported, along with a whole suit of
        // trigonometric and hyperbolic functions, including inverse functions.
        // the bug which automatically added a multiplication symbol in front of the first operator
        // inside a bracket is solved now, try asin(sin(90)){is in degree} for example
        // or any other expression to test.

        /* EXISTING CHARACTER OPERATORS : S, C, T, I, O, P, J, K, M, Q, R, U, s, c, t, i, o, p, j, k,
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
        EXPONENT('^', 3),
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
        SECANT_INVERSE('i', 4, TYPE.POST, "asec"),
        COSECANT_INVERSE('o', 4, TYPE.POST, "acsc"),
        COTANGENT_INVERSE('p', 4, TYPE.POST, "acot"),
        SINE_HYPERBOLA_INVERSE('j', 4, TYPE.POST, "asinh"),
        COSINE_HYPERBOLA_INVERSE('k', 4, TYPE.POST, "acosh"),
        TANGENT_HYPERBOLA_INVERSE('m', 4, TYPE.POST, "atanh"),
        SECANT_HYPERBOLA_INVERSE('q', 4, TYPE.POST, "asech"),
        COSECANT_HYPERBOLA_INVERSE('r', 4, TYPE.POST, "acsch"),
        COTANGENT_HYPERBOLA_INVERSE('u', 4, TYPE.POST, "acoth"),
        EE('G', 5, "E", true),
        RAND('D', 6, TYPE.CONSTANT, "rand"),
        e('e', 6, TYPE.CONSTANT, true),
        PI('π', 6, TYPE.CONSTANT),
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
        ADD_ALL('A', TYPE.FUNCTION, "add"),
        LOG_10('W', 5, TYPE.POST, "log10"),
        LOG_E('L', 5, TYPE.POST, "ln"),
        LOG_D('V', 5, "log"),
        AREA_OF_RECT('Z', TYPE.FUNCTION, "area[□]", 2, new int[]{
                operations.ARGUMENT_DOUBLE,
                operations.ARGUMENT_DOUBLE
        }),
        AREA_OF_CIRCLE('Y', TYPE.FUNCTION, "area[○]", 1, new int[]{
                operations.ARGUMENT_DOUBLE
        }),
        AREA_OF_TRIANGLE('F', TYPE.FUNCTION, "area[Δ]", 2, new int[]{
                operations.ARGUMENT_DOUBLE,
                operations.ARGUMENT_DOUBLE
        }),
        PERIMETER_OF_RECT('z', TYPE.FUNCTION, "peri[□]", 2, new int[]{
                operations.ARGUMENT_DOUBLE,
                operations.ARGUMENT_DOUBLE
        }),
        PERIMETER_OF_CIRCLE('y', TYPE.FUNCTION, "peri[○]", 1, new int[]{
            operations.ARGUMENT_DOUBLE
        }),
        PERIMETER_OF_TRIANGLE('f', TYPE.FUNCTION, "peri[Δ]", 3, new int[]{
                operations.ARGUMENT_DOUBLE,
                operations.ARGUMENT_DOUBLE,
                operations.ARGUMENT_DOUBLE
        }),
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
        public BigDecimal function(BigDecimal d1, BigDecimal d2){
            switch(operator){
                case '+' :
                    return d1.add(d2);
                case '-' :
                    return d1.subtract(d2);
                case '*' :
                    return d1.multiply(d2);
                case '/' :
                    return BigDecimal.valueOf(d1.doubleValue()/d2.doubleValue());
                case '^' :
                    return BigDecimal.valueOf(Math.pow(d1.doubleValue(), d2.doubleValue()));
                case 'S' :
                    return BigDecimal.valueOf(Math.sin(getAngle(d2.doubleValue(), false)));
                case 'C' :
                    return BigDecimal.valueOf(Math.cos(getAngle(d2.doubleValue(), false)));
                case 'T' :
                    return BigDecimal.valueOf(Math.tan(getAngle(d2.doubleValue(), false)));
                case '!' :
                    return BigDecimal.valueOf(factorial(d1.intValue()));
                case '√' :
                    return BigDecimal.valueOf(Math.pow(d2.doubleValue(), 1/d1.doubleValue()));
                case '%' :
                    return BigDecimal.valueOf(d1.doubleValue() % d2.doubleValue());
                case 'V' :
                    return BigDecimal.valueOf(Math.log(d2.doubleValue())/Math.log(d1.doubleValue()));
                case 'W' :
                    return BigDecimal.valueOf(Math.log10(d2.doubleValue()));
                case 'L' :
                    return BigDecimal.valueOf(Math.log(d2.doubleValue()));
                case 'I' :
                    return BigDecimal.valueOf(1/Math.cos(getAngle(d2.doubleValue(), false)));
                case 'O' :
                    return BigDecimal.valueOf(1/Math.sin(getAngle(d2.doubleValue(), false)));
                case 'P' :
                    return BigDecimal.valueOf(1/Math.tan(getAngle(d2.doubleValue(), false)));
                case 'J' :
                    return BigDecimal.valueOf(Math.sinh(d2.doubleValue()));
                case 'K' :
                    return BigDecimal.valueOf(Math.cosh(d2.doubleValue()));
                case 'M' :
                    return BigDecimal.valueOf(Math.tanh(d2.doubleValue()));
                case 'Q' :
                    return BigDecimal.valueOf(1/Math.cosh(d2.doubleValue()));
                case 'R' :
                    return BigDecimal.valueOf(1/Math.sinh(d2.doubleValue()));
                case 'U' :
                    return BigDecimal.valueOf(1/Math.tanh(d2.doubleValue()));
                case 's' :
                    return BigDecimal.valueOf(getAngle(Math.asin(d2.doubleValue()), true));
                case 'c' :
                    return BigDecimal.valueOf(getAngle(Math.acos(d2.doubleValue()), true));
                case 't' :
                    return BigDecimal.valueOf(getAngle(Math.atan(d2.doubleValue()), true));
                case 'i' :
                    return BigDecimal.valueOf(getAngle(Math.acos(1/d2.doubleValue()), true));
                case 'o' :
                    return BigDecimal.valueOf(getAngle(Math.asin(1/d2.doubleValue()), true));
                case 'p' :
                    return BigDecimal.valueOf(getAngle(Math.atan(1/d2.doubleValue()), true));
                case 'j' :
                    return BigDecimal.valueOf(Math.log(d2.doubleValue() +
                            Math.sqrt(Math.pow(d2.doubleValue(), 2) + 1)));
                case 'k' :
                    return BigDecimal.valueOf(Math.log(d2.doubleValue() +
                            Math.sqrt(Math.pow(d2.doubleValue(), 2) - 1)));
                case 'm' :
                    return BigDecimal.valueOf((1.0/2.0)*Math.log((1 + d2.doubleValue())/
                            (1 - d2.doubleValue())));
                case 'q' :
                    return BigDecimal.valueOf(Math.log((1 + Math.sqrt(1 - Math.pow(d2.doubleValue(), 2)))/
                            d2.doubleValue()));
                case 'r' :
                    return BigDecimal.valueOf(Math.log((1/d2.doubleValue()) +
                            ((Math.sqrt(1 + Math.pow(d2.doubleValue(), 2)))/Math.abs(d2.doubleValue()))));
                case 'u' :
                    return BigDecimal.valueOf((1.0/2.0)*Math.log((d2.doubleValue() + 1) /
                            (d2.doubleValue() - 1)));
                case 'G' :
                    return BigDecimal.valueOf(d1.doubleValue() * Math.pow(10, d2.doubleValue()));
                case 'D' :
                    return getRandom();
                case 'π' :
                    return BigDecimal.valueOf(Math.PI);
                case 'e' :
                    return BigDecimal.valueOf(Math.E);
            }
            return BigDecimal.ZERO;
        }

        /*
        * this function will be called when the type is TYPE.FUNCTION,
          it returns all the values seperated by comma inside the parentheses,
          parentheses is important to encapsulate the arguments of the function.
        */
        public BigDecimal function(argument[] arguments){
            switch(operator){
                case 'H' :
                    return BigDecimal.valueOf(Math.sqrt(Math.pow(arguments[0].BigDecimal_value.
                            doubleValue(), 2) + Math.pow(arguments[1].BigDecimal_value.doubleValue(), 2)));
                case 'B' :
                    return BigDecimal.valueOf(summation(arguments[0].BigDecimal_value.intValue(),
                            arguments[1].BigDecimal_value.intValue(),
                            arguments[2].string_value));
                case 'A' :
                    BigDecimal d = BigDecimal.valueOf(0);
                    for(argument ar : arguments){
                        d = d.add(ar.BigDecimal_value);
                    }
                    return d;
                case 'Z' :
                    return BigDecimal.valueOf(arguments[0].BigDecimal_value.doubleValue() *
                            arguments[1].BigDecimal_value.doubleValue());
                case 'Y' :
                    return BigDecimal.valueOf(Math.PI * Math.pow(arguments[0].BigDecimal_value.
                            doubleValue(), 2));
                case 'F' :
                    return BigDecimal.valueOf(1.0/2.0 * arguments[0].BigDecimal_value.doubleValue() *
                            arguments[1].BigDecimal_value.doubleValue());
                case 'z' :
                    return BigDecimal.valueOf(2*(arguments[0].BigDecimal_value.doubleValue() +
                            arguments[1].BigDecimal_value.doubleValue()));
                case 'y' :
                    return BigDecimal.valueOf(2 * Math.PI * arguments[0].BigDecimal_value.doubleValue());
                case 'f' :
                    return BigDecimal.valueOf(arguments[0].BigDecimal_value.doubleValue() +
                            arguments[1].BigDecimal_value.doubleValue() +
                            arguments[2].BigDecimal_value.doubleValue());
                case 'N' :
                    double[] values = new double[arguments.length];
                    for (int i = 0; i < arguments.length; i++){
                        values[i] = arguments[i].BigDecimal_value.doubleValue();
                    }
                    return BigDecimal.valueOf(HCF(values));
                case 'n' :
                    double[] values1 = new double[arguments.length];
                    for (int i = 0; i < arguments.length; i++){
                        values1[i] = arguments[i].BigDecimal_value.doubleValue();
                    }
                    return BigDecimal.valueOf(LCM(values1));
                case 'a' :
                    double[] values2 = new double[arguments.length];
                    for (int i = 0; i < arguments.length; i++){
                        values2[i] = arguments[i].BigDecimal_value.doubleValue();
                    }
                    return BigDecimal.valueOf(determinant(values2));
            }
            return BigDecimal.ZERO;
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
        public final int neutral_value = 1;
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
            //functionName = functionName.toUpperCase();
            this.functionName = functionName;
        }

        operations(char operator, int precedence, TYPE type, String functionName){
            this(operator, precedence, type, false);
            //functionName = functionName.toUpperCase();
            this.functionName = functionName;
        }

        operations(char operator, int precedence, String functionName, boolean strictCase){
            this(operator, precedence, TYPE.NONE, strictCase);
            //functionName = functionName.toUpperCase();
            this.functionName = functionName;
        }

        operations(char operator, int precedence, String functionName){
            this(operator, precedence, TYPE.NONE, false);
            //functionName = functionName.toUpperCase();
            this.functionName = functionName;
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

        operations(char operator, TYPE type, String functionName, boolean strictCase, int argumentSize,
                   int[] argumentType){
            this(operator, type, functionName, strictCase, argumentSize);
            this.argument_type = argumentType;
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
            public BigDecimal BigDecimal_value = BigDecimal.valueOf(0);
            public String string_value = "";
            public int argument_type = 1;

            public argument(BigDecimal BigDecimal_value, String string_value, int argument_type){
                this.BigDecimal_value = BigDecimal_value;
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

    public static BigDecimal getRandom(){
        return BigDecimal.valueOf(Math.random());
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

                        /*arguments[j] = new operations.argument(
                                whichOperator.argument_type[j] == operations.ARGUMENT_DOUBLE ?
                                BigDecimal.valueOf(Double.parseDouble(Evaluate(array[j]))) : BigDecimal.ZERO,
                                whichOperator.argument_type[j] == operations.ARGUMENT_STRING ? array[j] : "",
                                whichOperator.argument_type[j]*//*operations.ARGUMENT_DOUBLE*//*);*/

                        if(whichOperator.argumentSize == -1){
                            arguments[j] = new operations.argument(
                                    BigDecimal.valueOf(Double.parseDouble(Evaluate(array[j]))),
                                    "", operations.ARGUMENT_DOUBLE);
                        }else{
                            if(whichOperator.argument_type.length > 1){
                                switch (whichOperator.argument_type[j]) {
                                    case operations.ARGUMENT_DOUBLE:
                                        arguments[j] = new operations.argument(
                                                BigDecimal.valueOf(Double.parseDouble(Evaluate(array[j]))),
                                                "", operations.ARGUMENT_DOUBLE);
                                        break;
                                    case operations.ARGUMENT_STRING:
                                        arguments[j] = new operations.argument(BigDecimal.valueOf(0), array[j],
                                                operations.ARGUMENT_STRING);
                                        break;
                                    default:
                                        throw new IllegalStateException("the given argument type is illegal");
                                }
                            }else{
                                arguments[j] = new operations.argument(
                                        BigDecimal.valueOf(Double.parseDouble(Evaluate(array[j]))),
                                        "", operations.ARGUMENT_DOUBLE);
                            }
                        }
                    }
                    BigDecimal d = whichOperator.function(arguments);
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
        Stack<BigDecimal> operand = new Stack<>(2);
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
                    if(op.evaluateSymbol(expression.charAt(i)) && (i == expression.length()-1 ||
                            expression.charAt(i + 1) != '1')){
                        op1 = op;
                        operator_type = operations.TYPE.PRE;
                    }
                }else if(op.operator_type == operations.TYPE.CONSTANT){
                    if(op.evaluateSymbol(expression.charAt(i)) && (i == 0 || expression.charAt(i-1) != '1') &&
                            (i == expression.length()-1 || expression.charAt(i + 1) != '1')){
                        op1 = op;
                        operator_type = operations.TYPE.CONSTANT;
                    }
                }
            }
            switch (operator_type) {
                case POST:
                    if (i != 0 && expression.charAt(i - 1) != '(')
                        higherBuilder += "*";
                    higherBuilder += op1.neutral_value;
                    higherBuilder += op1.operator;
                    break;
                case PRE:
                    higherBuilder += op1.operator;
                    higherBuilder += op1.neutral_value;
                    if (i != expression.length() - 1 && expression.charAt(i + 1) != ')')
                        higherBuilder += "*";
                    break;
                case CONSTANT:
                    if (i != 0 && String.valueOf(expression.charAt(i-1)).matches("[1234567890]"))
                        higherBuilder += '*';
                    higherBuilder += op1.neutral_value;
                    higherBuilder += op1.operator;
                    higherBuilder += op1.neutral_value;
                    if (i != expression.length() - 1 && String.valueOf(expression.charAt(i+1)).
                            matches("[1234567890]"))
                        higherBuilder += '*';
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
                                    throw new IllegalStateException
                                            ("the second conflicting symbol is illegal");
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
                    BigDecimal value = BigDecimal.valueOf(Double.parseDouble(step_data.currentOperand));
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
                            BigDecimal operatedValue = operator.pullFirst().function(operand.pull(0),
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
        }
        operand.push(BigDecimal.valueOf(Double.parseDouble(step_data.currentOperand)));

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
            BigDecimal d = operator.pullFirst().function(operand.pull(0),
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

    private static BigDecimal simpleSolver(String expression, Stack<BigDecimal> operand,
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
                    BigDecimal value = BigDecimal.valueOf(Double.parseDouble(step_data.currentOperand));
                    operand.push(value);

                    if(operator.pullFirst() == null){
                        operator.push(whichOperator);
                        step_data.setStepData(whichOperator);
                    }else {
                        BigDecimal operatedValue = operator.pullFirst().function(operand.pull(0),
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
        operand.push(BigDecimal.valueOf(Double.parseDouble(step_data.currentOperand)));
        if (operator.isEmpty()){
            return operand.pullFirst();
        }
        BigDecimal d1 = operator.pullFirst().function(operand.pull(0),
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

