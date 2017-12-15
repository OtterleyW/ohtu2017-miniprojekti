/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author laapjuha
 */
public class ValidatorUtils {

    public static String returnValidUrl(String url) {
        String regex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|"
                + "https:\\/\\/)[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]"
                + "{2,5}(:[0-9]{1,5})?(\\/.*)?$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (!matcher.find()) {
            return "http://" + url;
        }

        return url;
    }

    public static boolean areParametersValid(String... params) {
        for (String str : params) {
            if (str == null || str.trim().isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
