// TranslatorServlet.java
package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TranslatorServlet")
public class TranslatorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve user input from the request
        String fromLanguage = request.getParameter("fromLanguage");
        String toLanguage = request.getParameter("toLanguage");
        String textToTranslate = request.getParameter("textToTranslate");

        // Call the Python program to process the input
        PythonScriptResult result = callPythonScript(fromLanguage, toLanguage, textToTranslate);

        // Set the content type to plain text with UTF-8 encoding
        response.setContentType("text/plain; charset=UTF-8");

        // Send the translation and pronunciation back to the web page
        response.getWriter().write(result.translatedText + (result.pronunciation != null ? " (" + result.pronunciation + ")" : ""));
    }

    // Method to call the Python script
 // Method to call the Python script
    private PythonScriptResult callPythonScript(String from, String to, String text) {
        try {
            // Specify the path to your Python interpreter
            String pythonInterpreterPath = "python";  // Adjust this path

            // Build the command to execute
            String command = pythonInterpreterPath + " translate.py " + from + " " + to + " \"" + text + "\"";

            // Set the working directory to the directory containing translate.py
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.directory(new File(getServletContext().getRealPath("/WEB-INF/classes/python_scripts")));

            // Execute the command
            Process process = processBuilder.start();

            // Read the output of the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // Parse the output and return a PythonScriptResult object
                String[] resultParts = output.toString().trim().split("\n");
                String translatedText = resultParts[0];
                String pronunciation = resultParts.length > 1 ? resultParts[1] : null;

                // Check if the text contains English letters
                boolean hasEnglishLetters = translatedText.matches(".*[a-zA-Z].*");

                return new PythonScriptResult(translatedText, pronunciation, hasEnglishLetters);
            } else {
                // Print the error stream
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                String errorLine;

                while ((errorLine = errorReader.readLine()) != null) {
                    System.out.println("Python script error: " + errorLine);
                    errorOutput.append(errorLine).append("\n");
                }

                // Check if any error occurred
                if (errorOutput.length() > 0) {
                    return new PythonScriptResult("Python script error: " + errorOutput.toString(), null, false);
                } else {
                    return new PythonScriptResult("Unknown Python script error", null, false);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new PythonScriptResult("Exception occurred: " + e.getMessage(), null, false);
        }
    }


    // Custom class to represent the result of the Python script
 // PythonScriptResult.java
    public class PythonScriptResult {
        public final String translatedText;
        public final String pronunciation;
        public final boolean hasEnglishLetters;

        public PythonScriptResult(String translatedText, String pronunciation, boolean hasEnglishLetters) {
            this.translatedText = translatedText;
            this.pronunciation = pronunciation;
            this.hasEnglishLetters = hasEnglishLetters;
        }
    }

}
