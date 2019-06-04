package ca.edtoaster;

import ca.edtoaster.components.Program;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream inputStream;

        if (args.length > 0) {
            String file = args[0];
            inputStream = new FileInputStream(file);
        } else {
            inputStream = System.in;
        }

        Program.from(inputStream).execute();
    }
}
