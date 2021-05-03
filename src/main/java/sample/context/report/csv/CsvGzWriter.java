package sample.context.report.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;

public class CsvGzWriter {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static final String LF_LINE_ENDING = "\n";

    /**
     *
     * @param fileName
     * @param dir
     * @param dtoList
     * @throws IOException
     */
    public static void execute(String fileName, String dir, List<Object> dtoList) throws IOException {

        List<String> csvRows = new ArrayList<>();
        Map<String, String> headerMap = objectMapper.convertValue(dtoList.get(0), new TypeReference<LinkedHashMap<String, String>>() {});
        csvRows.add("\"" + headerMap.entrySet()
                .stream()
                .map(x -> new SnakeCaseStrategy().translate(x.getKey()))
                .collect(Collectors.joining("\",\"")) + "\"" + LF_LINE_ENDING);
        dtoList.stream().forEach(y -> {
            Map<String, String> dataMap = objectMapper.convertValue(y, new TypeReference<LinkedHashMap<String, String>>() {});
            csvRows.add("\"" + dataMap.entrySet()
                    .stream()
                    .map(z -> z.getValue())
                    .collect(Collectors.joining("\",\"")) + "\"" + LF_LINE_ENDING);
        });

        Path pathDir = Paths.get(dir).toAbsolutePath();
        if (Files.notExists(pathDir)) {
            Files.createDirectories(pathDir);
        }

        Path path = pathDir.resolve(fileName + ".csv.gz");
        try (
                OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                GZIPOutputStream gzip = new GZIPOutputStream(os);
                OutputStreamWriter osw = new OutputStreamWriter(gzip, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(osw);) { // see https://qiita.com/n_slender/items/ef777ba3fe636c1ea2d6#%E6%9B%B8%E3%81%8D%E8%BE%BC%E3%81%BF
            bw.write(csvRows.stream().collect(Collectors.joining("")).replaceAll("null", ""));
        }
    }
}
