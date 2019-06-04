package ca.edtoaster.minimal;

import java.util.Scanner;

public class Brainfuck {
    private final int INFINITELY_LARGE_SIZE = 1000000;
    private final byte[] memory = new byte[INFINITELY_LARGE_SIZE];
    private int dataPointer = 0;

    public void start(String program) {
        char[] source = program.toCharArray();
        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < source.length; i++) {
            switch(source[i]) {
                case '>': dataPointer++; break;
                case '<': dataPointer--; break;
                case '+': memory[dataPointer]++; break;
                case '-': memory[dataPointer]--; break;
                case '.': System.out.print((char)memory[dataPointer]); break;
                case ',': memory[dataPointer] = scan.nextByte(); break;
                case '[':
                    if (memory[dataPointer] == 0) {
                        int stack = 0;
                        do { if(source[i] == '[' || source[i] == ']') stack += (92 - source[i]); i++; }
                        while (stack > 0);
                        i--;
                    }
                    break;
                case ']':
                    if (memory[dataPointer] != 0) {
                        int stack = 0;
                        do { if(source[i] == '[' || source[i] == ']') stack += (92 - source[i]); i--; }
                        while (stack < 0);
                    }
                    break;
                default: break;
            }
        }
    }
}
