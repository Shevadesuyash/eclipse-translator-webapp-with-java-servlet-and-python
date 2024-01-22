

package servlet;

public class PythonScriptResult {
    public String translatedText;
    public String pronunciation;
    public boolean hasEnglishLetters;

    public PythonScriptResult(String translatedText, String pronunciation, boolean hasEnglishLetters) {
        this.translatedText = translatedText;
        this.pronunciation = pronunciation;
        this.hasEnglishLetters = hasEnglishLetters;
    }
}
