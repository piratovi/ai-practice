package com.kolosov.aipractice.folderstructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.shell.command.annotation.Command;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Command
public class FolderStructureService {

    @Command(command = "structure")
    public void getFolderStructureAsJson(String path) throws IOException {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + path);
        }
        Map<String, Object> structure = buildFolderStructure(folder);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(structure);
        System.out.println(result);
    }

    private Map<String, Object> buildFolderStructure(File folder) {
        Map<String, Object> structure = new HashMap<>();
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                structure.put(file.getName(), buildFolderStructure(file));
            } else {
                structure.put(file.getName(), file.getAbsolutePath());
            }
        }
        return structure;
    }
}