/**
 * 
 */
package uk.bl.wa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileBuilder;
import com.optimaize.langdetect.profiles.LanguageProfileWriter;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class LanguageProfileCreator {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // create text object factory:
        TextObjectFactory textObjectFactory = CommonTextObjectFactories
                .forIndexingCleanText();

        // load your training text:
        TextObject inputText = textObjectFactory.create();
        long count = 0;
        // Scan files...
        for (File f : new File("profile-input").listFiles()) {
            System.out.println("Reading " + f.getName());
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    inputText.append(line);
                    System.out.println(f.getName() + ": " + line);
                    count += 1;
                }
            }
        }
        System.out.println("Read " + count + " lines.");

        // create the profile:
        LanguageProfile languageProfile = new LanguageProfileBuilder(
                LdLocale.fromString("gd"))
                        .ngramExtractor(NgramExtractors.standard())
                        .minimalFrequency(3) // adjust to tune profile size to
                                             // around 30KB
                        .addText(inputText).build();

        // store it to disk if you like:
        new LanguageProfileWriter().writeToDirectory(languageProfile,
                new File("profile-output"));
    }

}
