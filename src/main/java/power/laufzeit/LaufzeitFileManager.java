package power.laufzeit;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;

@Component
public class LaufzeitFileManager {
	private final Path filePath;

	public LaufzeitFileManager() throws IOException {
		this.filePath = Path.of("C:\\Users\\Timo\\Documents\\myrepos\\pwc\\src\\main\\resources\\data\\laufzeit.txt");

		if (Files.notExists(filePath)) {
			Files.createFile(filePath);
			updateNumber(0);
		}
	}

	public int readNumber() throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(filePath)) {
			String line = reader.readLine();
			return (line != null) ? Integer.parseInt(line.trim()) : 0;
		}
	}

	public void updateNumber(int newNumber) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
			writer.write(String.valueOf(newNumber));
		}
	}
}
