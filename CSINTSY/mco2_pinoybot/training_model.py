import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import accuracy_score, classification_report
from sklearn.utils import resample
import pickle

"""
PinoyBot Training Script
Trains a Multinomial Naive Bayes classifier to label tokens as ENG, FIL, or OTH
using 7 features: has_ng, has_symbol_or_number, vowel_ratio, end_vowel, has_double_consonants, has_prefix, has_hyphen.
"""
def extract_features(tokens):
    data = []
    for t in tokens:
        word = str(t)
        lower = word.lower()

        #Count alphabetic letters only
        letters = [c for c in word if c.isalpha()]

        #Checks if "ng" is present and does not end with "ing"
        has_ng = 1 if "ng" in lower and not lower.endswith("ing") else 0

        #Checks if the token has any symbols or numbers not in the alphabet
        has_symbol_numbers = 1 if any(not c.isalpha() for c in word) else 0

        #Gets the ratio: (number of vowels) / (number of letters)
        vowel_ratio = sum(c in "aeiou" for c in lower) / len(letters) if letters else 0.0

        #Checks if the token ends with a vowel 
        end_vowel = 1 if len(letters) > 0 and lower[-1] in "aeiou" else 0
        
        #Check if the token contains any double consonants ex. -rk, -ll, ch, -ld, etc.
        has_double_consonant = 1 if any(lower[i] not in "aeiou" and lower[i+1] not in "aeiou" for i in range(len(lower)-1)) else 0

        #For morphological cues
        has_prefix = 1 if lower.startswith(("nag", "na", "ma", "pa", "ka", "pin", "ni")) else 0
        has_hyphen = 1 if "-" in word else 0

        #Bundles the data together
        data.append([
            has_ng,
            has_symbol_numbers,
            vowel_ratio,
            end_vowel,
            has_double_consonant,
            has_prefix,
            has_hyphen
        ])
        #returns has_ng | has_symbol_or_number | vowel_ratio | end_vowel | has_prefix | has_hyphen
    return pd.DataFrame(data, columns=[
        "has_ng",
        "has_symbol_number",
        "vowel_ratio",
        "end_vowel",
        "has_double_consonant",
        "has_prefix",
        "has_hyphen"
    ])

# Load the Training data
df = pd.read_csv("final_annotations.csv")

# Separate by label
df_fil = df[df.label == "FIL"]
df_eng = df[df.label == "ENG"]
df_oth = df[df.label == "OTH"]

# Upsample ENG and OTH so they match FIL’s count (prevents model bias)
df_eng_upsampled = resample(df_eng, replace=True, n_samples=len(df_fil), random_state=42)
df_oth_upsampled = resample(df_oth, replace=True, n_samples=len(df_fil), random_state=42)

# Combine all into one balanced dataset
df_balanced = pd.concat([df_fil, df_eng_upsampled, df_oth_upsampled])

# Compute features
features = extract_features(df_balanced["word"])

# Combine features + labels
X = features
Y = df_balanced["label"]

# Split the data 70/15/15
X_train, X_temp, Y_train, Y_temp = train_test_split(X, Y, test_size=0.30, stratify=Y, random_state=42)

X_val, X_test, Y_val, Y_test = train_test_split(X_temp, Y_temp, test_size=0.50, stratify=Y_temp, random_state=42)

# Train Model
model = MultinomialNB()

model.fit(X_train, Y_train)

# 5. Evaluate
print("Validation Accuracy:", accuracy_score(Y_val, model.predict(X_val)))
print("Test Accuracy:", accuracy_score(Y_test, model.predict(X_test)))
print(classification_report(Y_test, model.predict(X_test)))

# 6. Save model
with open("trained_model.pkl", "wb") as f:
    pickle.dump(model, f)

print("Model saved as trained_model.pkl")