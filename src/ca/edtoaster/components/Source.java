package ca.edtoaster.components;

import ca.edtoaster.utils.SourceUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Stream;

public class Source {

    private String source;

    private Source(String source) {
        this.source = source;
    }

    public String toString() {
        return source;
    }

    public Stream<Character> stream() {
        return source.chars()
                .boxed()
                .map(i -> ((char) i.intValue()));
    }

    public static Source from(InputStream input, Charset charset) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, charset));
        StringBuilder fullSourceBuilder = new StringBuilder();

        String currentLine = null;
        while ((currentLine = bufferedReader.readLine()) != null) {
            fullSourceBuilder.append(currentLine);
        }

        String fullSource = SourceUtils.stripNonConformingCharacters(fullSourceBuilder.toString());
        return new Source(fullSource);
    }

    public static Source from(InputStream input) throws IOException {
        return from(input, Charset.defaultCharset());
    }

    public static Source from(String file) throws IOException {
        return from(new FileInputStream(file));
    }
}
