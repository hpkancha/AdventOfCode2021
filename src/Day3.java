import common.FileUtils;

import java.util.List;
import java.util.stream.Collectors;

import static common.Utils.convertBinaryToLong;

class Frequencies {
    int[] zerosFrequencies;
    int[] onesFrequencies;

    public Frequencies(int[] zerosFrequencies, int[] onesFrequencies) {
        this.zerosFrequencies = zerosFrequencies;
        this.onesFrequencies = onesFrequencies;
    }
}
public class Day3 {
    private static Frequencies getFrequencies(List<String> reportBinaries) {
        int length = reportBinaries.get(0).length();
        int[] zerosFrequencies = new int[length];
        int[] onesFrequencies = new int[length];

        for (String reportBinary: reportBinaries) {
            for (int i = 0; i < length; i++) {
                int bit = Integer.parseInt(reportBinary.substring(i, i+1));
                if (bit == 0) {
                    zerosFrequencies[i]++;
                } else {
                    onesFrequencies[i]++;
                }
            }
        }

        return new Frequencies(zerosFrequencies, onesFrequencies);
    }

    private static void computePowerConsumption(List<String> reportBinaries) {
        Frequencies f = getFrequencies(reportBinaries);
        String gammaRate = "";
        String epsilonRate = "";

        for (int i = 0; i < f.zerosFrequencies.length; i++) {
            if (f.zerosFrequencies[i] > f.onesFrequencies[i]) {
                gammaRate += 0;
                epsilonRate += 1;
            } else {
                gammaRate += 1;
                epsilonRate += 0;
            }
        }

        System.out.println(convertBinaryToLong(gammaRate) * convertBinaryToLong(epsilonRate));
    }

    private static String getOxygenGeneratorRating(List<String> reportBinaries) {
        int i = 0;
        while (reportBinaries.size() > 1) {
            Frequencies f = getFrequencies(reportBinaries);
            final int iCopy = i;
            if (f.zerosFrequencies[i] > f.onesFrequencies[i]) {
                reportBinaries = reportBinaries.stream().filter(r -> r.toCharArray()[iCopy] == '0')
                        .collect(Collectors.toList());
            } else {
                reportBinaries = reportBinaries.stream().filter(r -> r.toCharArray()[iCopy] == '1')
                        .collect(Collectors.toList());
            }
            i++;
        }

        return reportBinaries.get(0);
    }

    private static String getCO2ScrubberRating(List<String> reportBinaries) {
        int i = 0;
        while (reportBinaries.size() > 1) {
            Frequencies f = getFrequencies(reportBinaries);
            final int iCopy = i;
            if (f.zerosFrequencies[i] > f.onesFrequencies[i]) {
                reportBinaries = reportBinaries.stream().filter(r -> r.toCharArray()[iCopy] == '1')
                        .collect(Collectors.toList());
            } else {
                reportBinaries = reportBinaries.stream().filter(r -> r.toCharArray()[iCopy] == '0')
                        .collect(Collectors.toList());
            }
            i++;
        }

        return reportBinaries.get(0);
    }

    private static void computeLifeSupportRating(List<String> reportBinaries) {
        String oxygenGeneratorRating = getOxygenGeneratorRating(reportBinaries);
        String co2ScrubberRating = getCO2ScrubberRating(reportBinaries);

        System.out.println(convertBinaryToLong(oxygenGeneratorRating) * convertBinaryToLong(co2ScrubberRating));
    }

    public static void main(String[] args) {
        List<String> reportBinaries = FileUtils.readLinesFromFile("Day3.txt");

        //Day3_1
        computePowerConsumption(reportBinaries);

        //Day3_2
        computeLifeSupportRating(reportBinaries);
    }
}
