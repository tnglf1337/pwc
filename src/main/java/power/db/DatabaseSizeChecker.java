package power.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class DatabaseSizeChecker {

	private Path path = Path.of("powerconsumption-data");
	private final long TWO_GIGS = 2L * 1024 * 1024 * 1024;
	private final Logger logger = LoggerFactory.getLogger(DatabaseSizeChecker.class);

	@Scheduled(fixedRate = 7200000) //2h
	public void checkDatabaseSizeScheduled() {
		if (!checkDatabaseSize()) {
			logger.error("SCHEDULED: Die Datenbankgröße überschreitet 2 GB. Bitte bereinigen Sie die Datenbank.");
		} else {
			logger.info("SCHEDULED: Datenbankgröße ist in Ordnung.");
		}
	}

	public long getFolderSize(Path path) throws IOException {
		try (Stream<Path> pfade = Files.walk(path.toAbsolutePath())) {
			return pfade
					.filter(Files::isRegularFile)
					.mapToLong(p -> {
						try {
							return Files.size(p);
						} catch (IOException e) {
							logger.error("Fehler bei Datei: " + p);
							return 0L;
						}
					})
					.sum();
		}
	}

	public boolean checkDatabaseSize() {
		try {
			long size = getFolderSize(path);
			logger.info("Ordnergröße: " + size + "MB");
			return size < TWO_GIGS;
		} catch (IOException e) {
			logger.error("Fehler beim Berechnen der Ordnergröße: " + e.getMessage());
			return false;
		}
	}

	public int sizeInMb(long size) {
		return (int) (size / 1024.0 / 1024.0);
	}

	public int getSizeInMb() {
		try {
			return sizeInMb(getFolderSize(path));
		} catch (IOException e) {
			logger.error("Fehler beim Berechnen der Ordnergröße: " + e.getMessage());
			return -1; // Fehlercode
		}
	}
}
