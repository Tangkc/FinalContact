package com.jc.android.contact.presentation.widget;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class PinyinUtils {
    public static String getFull(String name) {
        try {

            if (TextUtils.isEmpty(name)) {
                return "";
            }

            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);

                String[] values = PinyinHelper.toHanyuPinyinStringArray(c, format);
                // System.out.print(Arrays.toString(values));
                if (values != null && values.length > 0) {
                    for (String value : values) {
                        builder.append(value);
                    }
                }

            }

            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
