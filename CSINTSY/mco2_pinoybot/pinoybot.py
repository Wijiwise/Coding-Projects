"""
pinoybot.py

PinoyBot: Filipino Code-Switched Language Identifier

This module provides the main tagging function for the PinoyBot project, which identifies the language of each word in a code-switched Filipino-English text. The function is designed to be called with a list of tokens and returns a list of tags ("ENG", "FIL", or "OTH").

Model training and feature extraction should be implemented in a separate script. The trained model should be saved and loaded here for prediction.
"""

import os
import pickle
import pandas as pd
from typing import List

# Set constant lists for morphological cues and English-like endings
# These use general linguistic patterns (affixes/endings) rather than a dictionary of specific words.
FILIPINO_PREFIXES = ("nag", "na", "ma", "pa", "ka", "pin", "ni")
FILIPINO_SUFFIXES = ("han", "hin", "an")
ENGLISH_LIKE_ENDINGS = ("er", "or", "tion", "ment", "ist", "ous", "ive", "ble", "ed", "ing" , "rk", "ll", "ch", "ld")

"""
Given a list of words (tokens), compute the same features used during training.
    Returns a DataFrame with columns:
    has_ng | has_symbol_or_number | vowel_ratio | end_vowel
"""
def extract_features(tokens):
    data = []
    for t in tokens:
        word = str(t)
        lower = word.lower()

        # Count alphabetic letters only
        letters = [c for c in word if c.isalpha()]

        # Checks if "ng" is present and does not end with "ing"
        has_ng = 1 if "ng" in lower and not lower.endswith("ing") else 0

        # Checks if the token has any symbols or numbers not in the alphabet
        has_symbol_numbers = 1 if any(not c.isalpha() for c in word) else 0

        # Gets the ratio: (number of vowels) / (number of letters)
        vowel_ratio = sum(c in "aeiou" for c in lower) / len(letters) if letters else 0.0

        # Checks if the token ends with a vowel 
        end_vowel = 1 if len(letters) > 0 and lower[-1] in "aeiou" else 0
        
        # Check if the token contains any double consonants ex. -rk, -ll, ch, -ld, etc.
        # Fixed logic: check for two consecutive non-vowels (consonants)
        has_double_consonant = 1 if any(lower[i] not in "aeiou" and lower[i+1] not in "aeiou" for i in range(len(lower)-1)) else 0
        
        # For morphological cues
        has_prefix = 1 if lower.startswith(FILIPINO_PREFIXES) else 0
        has_hyphen = 1 if "-" in word else 0

        # Bundles the data together
        data.append([
            has_ng,
            has_symbol_numbers,
            vowel_ratio,
            end_vowel,
            has_double_consonant,
            has_prefix,
            has_hyphen
        ])
    return pd.DataFrame(data, columns=[
        "has_ng",
        "has_symbol_number",
        "vowel_ratio",
        "end_vowel",
        "has_double_consonant",
        "has_prefix",
        "has_hyphen"
    ])

# Main tagging function
def tag_language(tokens: List[str]) -> List[str]:
    """
    Tags each token in the input list with its predicted language.
    Args:
        tokens: List of word tokens (strings).
    Returns:
        tags: List of predicted tags ("ENG", "FIL", or "OTH"), one per token.
    """
    # 1. Load the trained model
    model_path = "trained_model.pkl"
    if not os.path.exists(model_path):
        # Fallback: tag all as OTH if model not found, consistent with error handling
        print(f"Warning: Model file not found at {model_path}. Returning OTH for all tokens.")
        return ["OTH" for _ in tokens]
    with open(model_path, "rb") as f:
        model = pickle.load(f)

    # 2. Prefiltering (OTH detection)
    # Automatically mark tokens with no alphabetic characters as OTH (Symbols, Numbers)
    oth_mask = []
    clean_tokens = []
    for w in tokens:
        if not any(c.isalpha() for c in w):
            oth_mask.append(True)
            clean_tokens.append("")
        else:
            oth_mask.append(False)
            clean_tokens.append(w)

    # 3. Forced FIL (Intra-word code switched words, e.g., "nag-lunch")
    forced_fil = []
    for w in tokens:
        lw = w.lower()
        # Check if word has a Filipino prefix followed by a hyphen
        if "-" in lw and any(lw.startswith(p + "-") for p in FILIPINO_PREFIXES):
            forced_fil.append(True)
        else:
            forced_fil.append(False)

    # 4. Extract features for non-OTH tokens and predict
    non_oth_tokens = [w for w, is_oth in zip(clean_tokens, oth_mask) if not is_oth]
    features = extract_features(non_oth_tokens)
    predicted_labels = list(model.predict(features)) if not features.empty else []

    # 5. Filipino prefix and suffix adjustment (Morphological Cue Adjustment)
    adjusted_labels = []

    for word, pred in zip(non_oth_tokens, predicted_labels):
        lower = word.lower()
        
        # Check for Filipino morphological cues (Prefixes/Suffixes)
        is_prefix = any(lower.startswith(p) and len(lower) - len(p) >= 3 for p in FILIPINO_PREFIXES)
        is_suffix = any(lower.endswith(s) and len(lower) - len(s) >= 3 for s in FILIPINO_SUFFIXES)

        # Adjust prediction to FIL if morphological cues are present
        if (is_prefix or is_suffix) and pred != "OTH":
            adjusted_labels.append("FIL")
        else:
            adjusted_labels.append(pred)
    predicted_labels = adjusted_labels # Updated predictions

    # 6. English-Like Recheck (Based on endings only)
    rechecked_labels = []
    for word, label in zip(non_oth_tokens, predicted_labels):
        lower = word.lower()
        
        # If labeled FIL but has English-like endings, change to ENG
        if label == "FIL" and lower.endswith(ENGLISH_LIKE_ENDINGS):
            rechecked_labels.append("ENG")
        # Otherwise, keep original label
        else:
            rechecked_labels.append(label)
    predicted_labels = rechecked_labels

    # 7. Final Assembly and OTH/Rule application
    final_labels = []
    idx = 0

    # Combine all adjustments and masks to produce final labels
    for w, is_oth, forced in zip(tokens, oth_mask, forced_fil):
        # OTH: directly assign (Symbols/Numbers)
        if is_oth:
            final_labels.append("OTH")
            continue

        # Get the predicted label from earlier steps
        label = predicted_labels[idx] if idx < len(predicted_labels) else "OTH" # Default to OTH if indices mismatch
        idx += 1

        # Final adjustments
        if forced:
            # Forced FIL for intra-word CS (e.g., nag-lunch)
            final_labels.append("FIL")
        elif w.isupper() and len(w) <= 5:
            # OTH for short, fully capitalized words (Acronyms/Abbreviations)
            final_labels.append("OTH")
        else:
            # Assign the final model-predicted and morphologically-adjusted label
            final_labels.append(str(label))

    # 8. Return the final list
    return final_labels

if __name__ == "__main__":
    test_cases = [
        # Filipino Prefix Tests
        ["pahingi", "me", "ng", "water"],
        ["nag-aaral", "ako", "sa", "DLSU"],
        ["pinush", "ni", "teacher"],
        ["nainlove", "siya"],

        # English-Looking Words with Morphological Cues
        # Removed hardcoded words, relying on endings and model only
        ["paper", "painter", "manager", "player"],
        ["maket", "paliguan", "maganda"], # paliguan/maganda should be FIL due to suffix/prefix

        # English with Suffixes
        ["teacher", "leader", "speaker"],
        ["kainin", "sabihan", "utusan"], # These are Filipino words with suffixes, should be FIL

        # OTH / Symbols / Numbers / Acronyms
        ["Hello", "123", "!", "haha"], # haha should rely on model features, not dictionary
        ["Good", "morning", ":)", "sir"],
        ["DPWH", "project", "ongoing", "sa", "Brgy."],

        # Named Entity / Proper Noun Contexts
        ["Rizal", "Park", "ay", "malinis", "."],
        ["Facebook", "post", "ko"],
        ["Pinoy", "pride", "event"],

        # Code-switched cases
        ["nag-lunch", "kami", "sa", "office"],
        ["Please", "send", "the", "paper", "later"],

        # Additional Cases
        ["park"],
    ]
    
    # Note: Since the trained_model.pkl is not available here, 
    # the output will rely on the fallback or general logic.
    print("--- Running Tests ---")
    for tokens in test_cases:
        print("Tokens:", tokens)
        print("Tags:", tag_language(tokens))
        print()
