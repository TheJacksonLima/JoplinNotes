package org.jfl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {
        var context = SpringApplication.run(Main.class, args);
        Path p = Path.of("day25.txt");

        Files.writeString( p, "RentMap\n" );
        System.out.println( Files.readString(p));
        System.out.println(p.toAbsolutePath());
        Files.writeString(p, "Payment approved");
        Files.writeString(p, "Payment 2\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        String content = Files.readString(p);
        List<String> lines = Files.readAllLines(p);

        for (String line : lines) {
            System.out.println(line);
        }

        for (String line : lines) {
            System.out.println(line);
        }

        try (BufferedReader reader = Files.newBufferedReader(p)) {
            String line;
            while ((line =
                    reader.readLine()) != null) {

                System.out.println(line);
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(p)) {

            writer.write("PAY-2026-123456");
            writer.newLine();
            writer.write("PAY-2026-654321");
        }

        //Files.deleteIfExists(p);




    }

}