/**
 * 
 */
package uk.bl.wa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
        // Scan files...
        for (File f : new File("profile-input").listFiles()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    inputText.append(line);
                }
            }
        }

        // create the profile:
        LanguageProfile languageProfile = new LanguageProfileBuilder(
                LdLocale.fromString("gd"))
                        .ngramExtractor(NgramExtractors.standard())
                        .minimalFrequency(2) // adjust to tune profile size to
                                             // around 30KB
                        .addText(inputText).build();

        // store it to disk if you like:
        new LanguageProfileWriter().writeToDirectory(languageProfile,
                new File("profile-output"));
    }

}
