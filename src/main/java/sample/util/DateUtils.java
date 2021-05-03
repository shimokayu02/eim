package sample.util;

import java.util.AbstractMap;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * DateUtils。
 *
 * <p>low: クライアント側での Date の扱われ方が良く分からなかったため Java の方で力業。</p>
 */
public abstract class DateUtils {

    public static String convert(String strDate) { // Thu Jan 01 1970 09:00:00 GMT+0900 (日本標準時)

        if (StringUtils.isEmpty(strDate) || strDate.equals("null") || strDate.length() != 41) {
            return null;
        }

        Map<String, String> months = monthsMap().entrySet()
                .stream()
                .map(x -> new AbstractMap.SimpleEntry<String, String>(x.getValue(), x.getKey()))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        String yyyyMMdd = strDate.substring(11, 15) + months.get(strDate.substring(4, 7)) + strDate.substring(8, 10); // 19700101

        return Checker.chkYmd(yyyyMMdd) ? yyyyMMdd : null;
    }

    public static String processed(String yyyyMMdd) { // 19700101
        if (!Checker.chkYmd(yyyyMMdd)) {
            return null;
        }
        return yyyyMMdd.substring(0, 4) + "年" + yyyyMMdd.substring(4, 6) + "月" + yyyyMMdd.substring(6, 8) + "日"; // 1970年01月01日
    }

    public static String revertYmd(String yyyyMMdd) { // 19700101
        if (!Checker.chkYmd(yyyyMMdd)) {
            return null;
        }
        return getDayOfWeek(Integer.valueOf(yyyyMMdd.substring(0, 4)), Integer.valueOf(yyyyMMdd.substring(4, 6)), Integer.valueOf(yyyyMMdd.substring(6, 8))) +
                " " +
                monthsMap().get(yyyyMMdd.substring(4, 6)) +
                " " +
                yyyyMMdd.substring(6, 8) +
                " " +
                yyyyMMdd.substring(0, 4) + " 09:00:00 GMT+0900 (日本標準時)"; // Thu Jan 01 1970 09:00:00 GMT+0900 (日本標準時)
    }

    private static String getDayOfWeek(Integer yyyy, Integer MM, Integer dd) {
        String dayOfWeek = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(yyyy, MM - 1, dd);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.SUNDAY:
            dayOfWeek = "Sun";
            break;
        case Calendar.MONDAY:
            dayOfWeek = "Mon";
            break;
        case Calendar.TUESDAY:
            dayOfWeek = "Tue";
            break;
        case Calendar.WEDNESDAY:
            dayOfWeek = "Wed";
            break;
        case Calendar.THURSDAY:
            dayOfWeek = "Thu";
            break;
        case Calendar.FRIDAY:
            dayOfWeek = "Fri";
            break;
        case Calendar.SATURDAY:
            dayOfWeek = "Sat";
            break;
        }
        return dayOfWeek;
    }

    private static Map<String, String> monthsMap() {
        Map<String, String> months = new HashMap<>();
        months.put("01", "Jan");
        months.put("02", "Feb");
        months.put("03", "Mar");
        months.put("04", "Apr");
        months.put("05", "May");
        months.put("06", "Jun");
        months.put("07", "Jul");
        months.put("08", "Aug");
        months.put("09", "Sep");
        months.put("10", "Oct");
        months.put("11", "Nov");
        months.put("12", "Dec");
        return months;
    }

}
