package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<Integer> readNumsFromFile(String filePath) {
        List<Integer> retList = new ArrayList();
        for (String line : readLinesFromFile(filePath)) {
            retList.add(Integer.parseInt(line));
        }
        return retList;
    }
    public static List<String> readLinesFromFile(String filePath) {
        List<String> retList = new ArrayList();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader("/Volumes/workplace/AdventOfCode2021/resources/" + filePath));
            while ((line = br.readLine()) != null){
                retList.add(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return retList;
    }
}
