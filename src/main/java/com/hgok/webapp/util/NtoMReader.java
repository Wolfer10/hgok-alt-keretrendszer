package com.hgok.webapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NtoMReader {

    String fileName;

    public String getStringNthLine(String source, int line ) {
        Pattern NEWLINE = Pattern.compile("\\R");
        return  NEWLINE.split(source)[line - 1];
    }

    public  String readFromNLineToMCharacter(BufferedReader br, int from, int to) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        skipNLine(br, from);
        for (int i = from; i <= to; i++) {
            stringBuilder.append((char) br.read());
        }
        return stringBuilder.toString();
    }

    public String readFromNtoEnd(int from) {
        StringBuilder stringBuilder = new StringBuilder();
        try(BufferedReader br = (BufferedReader) openFile()){
            skipNLine(br, from - 1);
            readCharsIntoStringBuilder(stringBuilder, br);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        return stringBuilder.toString();
    }

    protected Reader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileName));
    }

    private static void readCharsIntoStringBuilder(StringBuilder stringBuilder, BufferedReader br) throws IOException {
        int c;
        while((c = br.read()) != -1)
        {
            char character = (char) c;
            stringBuilder.append(character);
        }
    }

    private static void skipNLine(BufferedReader br, int n) throws IOException {
        for (int i = 0; i < n; i++) {
            br.readLine();
        }
    }
}