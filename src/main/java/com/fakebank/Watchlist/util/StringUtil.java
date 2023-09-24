package com.fakebank.Watchlist.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern ALPHABETIC_CHARACTERS_PATTERN = Pattern.compile("[^a-zA-Z]");
    private static final Pattern NONALPHABETIC_LEADING_AND_TRAILING_PATTERN = Pattern.compile("^[^a-zA-Z]+|[^a-zA-Z]+$");

    public static List<String> splitStringByWhiteSpaces(String input) {
        return new ArrayList<>(Arrays.asList(input.split("\\s+")));
    }

    public static String removeNonAlphabeticalCharacters(String input) {
        Matcher matcher = ALPHABETIC_CHARACTERS_PATTERN.matcher(input);
        return matcher.replaceAll("");
    }

    public static String trimNonAlphabeticalCharacters(String input) {
        Matcher matcher = NONALPHABETIC_LEADING_AND_TRAILING_PATTERN.matcher(input);
        return matcher.replaceAll("");
    }

    public static Set<String> getPermutations(List<String> input) {
        Set<String> permutations = new HashSet<>();
        findPermutationsRecursive(input, new ArrayList<>(), permutations);
        return permutations;
    }

    private static void findPermutationsRecursive(List<String> remaining, List<String> currentPermutation, Set<String> permutations) {
        if (remaining.isEmpty()) {
            permutations.add(String.join(" ", currentPermutation));
            return;
        }

        for (int i = 0; i < remaining.size(); i++) {
            String element = remaining.get(i);
            currentPermutation.add(element);
            List<String> nextRemaining = new ArrayList<>(remaining);
            nextRemaining.remove(i);
            findPermutationsRecursive(nextRemaining, currentPermutation, permutations);
            currentPermutation.remove(currentPermutation.size() - 1);
        }
    }

}
