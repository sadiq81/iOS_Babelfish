// Imports the Google Cloud client library

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class BabelFish {

    private static List<String> supportedLanguages = Arrays.asList("de", "sv", "nb", "en");
    private static String sourceLanguage = "da";
    private static String sourceLanguageFileFolder = "/Users/tommysadiqhinrichsen/junkbusters-ios/junkbusters/junkbusters/Ressources/Localization/";
    private static String fileSuffix = ".lproj/Localizable.strings";

    public static void main(String... args) throws Exception {
        // Instantiates a client

        for (String targetLanguage : supportedLanguages) {

            Translate translate = TranslateOptions.getDefaultInstance().getService();
            List<TranslateOption> optionList = Arrays.asList(TranslateOption.sourceLanguage(sourceLanguage), TranslateOption.targetLanguage(targetLanguage));
            TranslateOption[] options = optionList.toArray(new TranslateOption[optionList.size()]);

            String path = sourceLanguageFileFolder + sourceLanguage + fileSuffix;
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = "";
            String splitBy = "\\\"\\s*=\\s*\\\"";

            FileWriter fw = new FileWriter(sourceLanguageFileFolder + targetLanguage + fileSuffix);
            BufferedWriter bw = new BufferedWriter(fw);

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] translationLine = line.split(splitBy);
                if (translationLine.length == 2) {

                    String key = translationLine[0];
                    String text = translationLine[1];
                    String trimmed = text.substring(0, text.length() - 2);

                    Translation translation = translate.translate(trimmed, options);
                    String translatedText = translation.getTranslatedText();
                    String entry = key + "\" = \"" + translatedText + "\";\n";
                    bw.write(entry);
                } else {
                    bw.write(line);
                }
            }
            bw.close();
            fw.close();
        }
    }
}
