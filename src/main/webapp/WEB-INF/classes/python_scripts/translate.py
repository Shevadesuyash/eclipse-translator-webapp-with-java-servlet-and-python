# translate.py
from googletrans import Translator, LANGUAGES
import sys

def detect_language(text_to_detect):
    try:
        translator = Translator()
        detected_language = translator.detect(text_to_detect).lang
        return detected_language
    except Exception as e:
        return f"Language detection error: {str(e)}"

def translate_text(from_language, to_language, text_to_translate):
    try:
        translator = Translator()

        if from_language == "auto":
            # Detect the input language
            from_language = detect_language(text_to_translate)

        translation = translator.translate(text_to_translate, src=from_language, dest=to_language)

        translated_text = translation.text
        pronunciation = translation.pronunciation if from_language == "en" else None

        return translated_text, pronunciation
    except Exception as e:
        return f"Translation error: {str(e)}", None

if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Usage: python translate.py from_language to_language text_to_translate")
        sys.exit(1)

    from_language = sys.argv[1]
    to_language = sys.argv[2]
    text_to_translate = sys.argv[3]

    translated_text, pronunciation = translate_text(from_language, to_language, text_to_translate)

    # Print the translated text with UTF-8 encoding
    sys.stdout.buffer.write(translated_text.encode('utf-8'))

    # Print the pronunciation with UTF-8 encoding (only for English input)
    if pronunciation:
        sys.stdout.buffer.write(f" ({pronunciation})".encode('utf-8'))
