package nl.han.jefmk.levels;

import com.google.gson.Gson;
import nl.han.jefmk.levels.model.LevelData;
import nl.han.jefmk.levels.model.LevelMeta;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LevelLoader {

    private static final String LEVELS_ROOT = "levels";
    private final Gson gson = new Gson();

    public LevelData load(String levelName) {
        String resourcePath = LEVELS_ROOT + "/" + levelName + ".json";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Level not found: " + resourcePath);
            }

            return getLevelData(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read level: " + resourcePath, e);
        }
    }

    public List<LevelMeta> getAvailableLevels() {
        try {
            URL folderUrl = getClass().getClassLoader().getResource(LEVELS_ROOT);
            if (folderUrl == null) return List.of();

            URI uri = folderUrl.toURI();

            // JAR check in case is running as JAR file
            if ("jar".equals(uri.getScheme())) {
                try (FileSystem fs = FileSystems.newFileSystem(uri, Map.of());
                     Stream<Path> files = Files.list(fs.getPath(LEVELS_ROOT))) {
                    return collectLevels(files);
                }
            } else {
                try (Stream<Path> files = Files.list(Path.of(uri))) {
                    return collectLevels(files);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to list levels: ", e);
        }
    }

    private List<LevelMeta> collectLevels(Stream<Path> files) {
        return files
                .filter(path -> path.toString().endsWith(".json"))
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    String resourcePath = LEVELS_ROOT + "/" + fileName;
                    try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                        LevelData levelData = getLevelData(reader);
                        return new LevelMeta(levelData.getName(), resourcePath);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read level: " + path, e);
                    }
                })
                .toList();
    }

    private LevelData getLevelData(Reader reader) {
        return gson.fromJson(reader, LevelData.class);
    }
}
