package ru.itmo.game.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Serializer {

    private final ObjectMapper objectMapper;
    private final Path pathToDirectory;

    public Serializer(Path pathToDirectory1)  {
        this.pathToDirectory = pathToDirectory1;
        objectMapper = new ObjectMapper();
    }

    public String serialize(Object object) throws IOException {
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("Object is not serializable");
        }
        return objectMapper.writeValueAsString(object);
    }


    public <T> T deserialize(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    public void serializeToFile(Object object, String filename) throws IOException {
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("Object is not serializable");
        }

        if (!Files.exists(pathToDirectory)){
            throw new IllegalArgumentException("Provided path does not exist");
        }


        String path = pathToDirectory + File.separator + filename;
        FileOutputStream out = new FileOutputStream(path);
        out.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object).getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    public <T> T deserializeFromFile(String filename, Class<T> clazz) throws IOException {
        if (!Files.exists(pathToDirectory)) {
            throw new IllegalArgumentException("Provided path does not exist");
        }
        String path = pathToDirectory + File.separator + filename;
        return objectMapper.readValue(new File(path), clazz);
    }

}
