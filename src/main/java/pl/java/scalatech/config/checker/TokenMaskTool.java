package pl.java.scalatech.config.checker;

import org.springframework.util.StringUtils;

final class TokenMaskTool {

    private TokenMaskTool() {
        throw new AssertionError();
    }

    static String maskString(String strText, int start, char maskChar) {
        int end = strText.length();
        if (!StringUtils.hasText(strText))
            return "";

        if (start < 0)
            start = 0;

        if (start > end)
            throw new IllegalArgumentException("End index cannot be greater than start index");

        int maskLength = end - start;

        if (maskLength == 0)
            return strText;

        StringBuilder sbMaskString = new StringBuilder(maskLength);

        for (int i = 0; i < maskLength; i++) {
            sbMaskString.append(maskChar);
        }

        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }
}
