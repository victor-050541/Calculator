import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainProgram {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        System.out.println(calculate(input));
    }

    public static String calculate(String input) throws Exception {
        convert converter = new convert();
        String answer;

        char[] charInput = input.toCharArray();

        int firstNumber;
        int secondNumber;
        int isCorrectSign = 0;

        Character currentSign = ' ';
        String[] component = input.split(" ");

        if(component.length > 3 || component.length <= 1)
            throw new Exception("Cтрока не является математической операцией");

        for(int i = 0; i < charInput.length; i++){
            if(charInput[i] == '+' || charInput[i] == '-' || charInput[i] == '*' || charInput[i] == '/'){
                currentSign = charInput[i];
                isCorrectSign++;
            }
        }

        if(isCorrectSign != 1){
            throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)!");
        }

        if(currentSign == '+'){
            component = input.split(" \\+ ");
        } else if (currentSign == '-') {
            component = input.split(" - ");
        } else if (currentSign == '*') {
            component = input.split(" \\* ");
        } else if (currentSign == '/') {
            component = input.split(" / ");
        }

        if(!converter.isRoman(component[0]) && !converter.isRoman(component[1])){
            firstNumber = Integer.parseInt(component[0]);
            secondNumber = Integer.parseInt(component[1]);

            answer = Integer.toString(converter.calcInArabic(firstNumber, secondNumber, currentSign));

        } else if (!converter.isRoman(component[0]) == !converter.isRoman(component[1])) {

            firstNumber = converter.romanToArab(component[0]);
            secondNumber = converter.romanToArab(component[1]);

            int result = converter.calcInArabic(firstNumber, secondNumber, currentSign);

            if(result > 0)
                return converter.arabToRoman(result);
            else
                throw new ArithmeticException("В римской системе нет отрицательных чисел!");

        } else {
            throw new Exception("используются одновременно разные системы счисления");
        }

        return answer;
    }

}

class convert {
    private HashMap<Character, Integer> romanMap = new HashMap<>();

    public convert(){

        romanMap.put('I', 1);
        romanMap.put('V', 5);
        romanMap.put('X', 10);
        romanMap.put('L', 50);
        romanMap.put('C', 100);
        romanMap.put('D', 500);
        romanMap.put('M', 1000);

    }

    public boolean detectRange(int number){
        if(number >= 1 && number <= 10) {
            return true;
        }
        else{
            return false;
        }
    }
    public int calcInArabic(int firstNumber, int secondNumber, char currentSign) throws IOException {

        int answer = -1;
        if(detectRange(firstNumber) && detectRange(secondNumber)){
            switch (currentSign) {
                case ('+') -> {
                    answer = firstNumber + secondNumber;
                }
                case ('-') -> {
                    answer = firstNumber - secondNumber;
                }
                case ('*') -> {
                    answer = firstNumber * secondNumber;
                }
                case ('/') -> {
                    try {
                        answer = firstNumber / secondNumber;
                    } catch (ArithmeticException | InputMismatchException e) {
                        System.err.println("Exception : " + e);
                        System.err.println("Результатом операции могут быть только целые числа!");
                    }
                }
                default -> throw new IllegalArgumentException("Неверный знак операции");
            }
        } else {
            throw new IOException("Числа не находятся в диапазоне от 1 до 10!");

        }
        return answer;
    }

    public boolean isRoman(String number){
        if(romanMap.containsKey(number.charAt(0))){
            return true;
        } else {
            return false;
        }
    }

    public int romanToArab(String roman) {
        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {

            int currentValue = romanMap.get(roman.charAt(i));
            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }
            prevValue = currentValue;
        }

        return result;
    }

    public String arabToRoman(int arabic){

        String[] symbol = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (arabic >= values[i]) {
                roman.append(symbol[i]);
                arabic -= values[i];
            }
        }

        return roman.toString();
    }
}