#Check if the token contains any double consonants ex. -rk, -ll, ch, -ld, etc.
        has_double_consonant = 1 if any(lower[i] and lower[i+1] not in "aeiou" for i in range(len(lower)-1)) else 0