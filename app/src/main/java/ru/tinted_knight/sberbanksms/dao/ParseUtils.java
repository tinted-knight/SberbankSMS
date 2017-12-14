package ru.tinted_knight.sberbanksms.dao;

import android.telephony.SmsMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;

public class ParseUtils {

    private static final class DateTimeKeys {
        public static final int YEAR = 0;
        public static final int MONTH = 1;
        public static final int DAY = 2;
        public static final int HOUR = 3;
        public static final int MINUTE = 4;
    }

    private static FullMessageEntity sReturnEntity;

    public static String parseFromPdus(Object[] pdus) {
        final SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }

        String sender = messages[0].getDisplayOriginatingAddress();
//        long dateReceived = messages[0].getTimestampMillis();
        if (Constants.SBER_PHONE_NUMBER.equalsIgnoreCase(sender)) {
            StringBuilder raw = new StringBuilder();
            for (SmsMessage message : messages)
                raw.append(message.getMessageBody());
            return raw.toString();
        }
        return null;
    }

    public static FullMessageEntity fromStringToEntity(String data) {
        if (tryExtractData(data, Constants.rublePattern)
                || tryExtractData(data, Constants.dollarPattern)
                || tryExtractData(data, Constants.euroPattern)) {

            return sReturnEntity;
        }
//        ParseUtils.tryExtractSmth(this, data);
        return null;
    }

    public static boolean tryExtractData(String data, String patternString) {
        Matcher matcher = Pattern.compile(patternString).matcher(data);
        if (matcher.find()) {
            sReturnEntity = new FullMessageEntity();
            if (data.contains(Constants.MOBILE_BANK_FLAG)
                    && extractMobileBank(data)) {
                return true;
            }
            try {
                sReturnEntity.card = matcher.group(2).trim();
                sReturnEntity.cardType = cardTypeFromString(matcher.group(1).trim());

                String dateString;
                if (matcher.group(4) != null) { // 3-ей группы может не быть
                    dateString = matcher.group(3).trim() + " " + matcher.group(4).trim();
                } else {
                    dateString = matcher.group(3).trim() + " 00:00";
                }
                DateFormat dateFormat = new SimpleDateFormat(Constants.smsDateFormat, Locale.getDefault());
                sReturnEntity.date = dateFormat.parse(dateString).getTime() / 1000;

                String typeString = matcher.group(5).trim();
                sReturnEntity.type = Constants.OperationType.getType(typeString);

                sReturnEntity.agent = matcher.group(7).trim();
                sReturnEntity.summa = summaFromString(matcher.group(6).trim());
                sReturnEntity.balance = balanceFromString(matcher.group(9).trim());

                int[] dtSplit = getDateSplit(sReturnEntity.date);
                sReturnEntity.year = dtSplit[DateTimeKeys.YEAR];
                sReturnEntity.month = dtSplit[DateTimeKeys.MONTH];
                sReturnEntity.day = dtSplit[DateTimeKeys.DAY];
                sReturnEntity.hour = dtSplit[DateTimeKeys.HOUR];
                sReturnEntity.minute = dtSplit[DateTimeKeys.MINUTE];

                return true;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void tryExtractSmth(String data) {
        if (tryExtractPassword(data))
            return;
        if (tryExtractInTransaction(data))
            return;
    }

    private static boolean tryExtractInTransaction(String data) {
        Matcher matcher = Pattern.compile(Constants.inTransactionPattern).matcher(data);
        if (matcher.find()) {
            // перевод средств между картами входящий
            return true;
        }
        return false;
    }

    private static boolean tryExtractPassword(String data) {
        Matcher matcher = Pattern.compile(Constants.passwordPattern).matcher(data);
        if (matcher.find()) {
            // есть слово "пароль" в тексте - не делаем ничего
//            this.cardNumber = null;
//            this.mCardType = Constants.CardType.NONE;
//            this.date = 0;
//            this.agent = null;
//            this.summa = null;
//            this.balance = null;
//            this.type = Constants.OperationType.ERRORCODE;
            return true;
        }
        return false;
    }


    private static boolean extractMobileBank(String data) {
        Matcher matcher = Pattern.compile(Constants.mobileBankPattern).matcher(data);
        if (matcher.find()) {
            try {
                sReturnEntity.card = matcher.group(2).trim();
                sReturnEntity.cardType = cardTypeFromString(matcher.group(1).trim());

                String dateString;
                dateString = matcher.group(3).trim() + " 00:00";
                DateFormat dateFormat = new SimpleDateFormat(Constants.smsDateFormat, Locale.getDefault());
                sReturnEntity.date = dateFormat.parse(dateString).getTime() / 1000;

                sReturnEntity.agent = "Сбербанк " + matcher.group(4).trim() + "-" + matcher.group(5).trim();
                sReturnEntity.type = Constants.OperationType.OUTCOME;
                sReturnEntity.summa = summaFromString(matcher.group(6).trim());
                sReturnEntity.balance = balanceFromString(matcher.group(9).trim());

                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String cardTypeFromString(String cardTypeString) {
        Constants.CardType cardType;
        try {
            cardType = Constants.CardType.valueOf(cardTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            cardType = Constants.CardType.NONE;
        }
        return cardType.toString();
    }

    private static float summaFromString(String sumString) {
        return Float.valueOf(sumString.substring(0, sumString.length()));
    }

    private static float balanceFromString(String balanceString) {
        return Float.valueOf(balanceString.substring(0, balanceString.length() - 1));
    }

    private static int[] getDateSplit(long date) {
//        HashMap<String, Integer> map = new HashMap<>();
        int[] result = new int[5];

        Date dateObject = new Date(date * 1_000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());

        sdf.applyPattern("yyyy");
        String year = sdf.format(dateObject);

        sdf.applyPattern("MM");
        String month = sdf.format(dateObject);

        sdf.applyPattern("dd");
        String day = sdf.format(dateObject);

        sdf.applyPattern("HH");
        String hour = sdf.format(dateObject);

        sdf.applyPattern("mm");
        String minute = sdf.format(dateObject);

        result[DateTimeKeys.YEAR] = Integer.valueOf(year);
        result[DateTimeKeys.MONTH] = Integer.valueOf(month);
        result[DateTimeKeys.DAY] = Integer.valueOf(day);
        result[DateTimeKeys.HOUR] = Integer.valueOf(hour);
        result[DateTimeKeys.MINUTE] = Integer.valueOf(minute);

//        map.put(Constants.SmsMapKeys.YEAR, Integer.valueOf(year));
//        map.put(Constants.SmsMapKeys.MONTH, Integer.valueOf(month));
//        map.put(Constants.SmsMapKeys.DAY, Integer.valueOf(day));
//        map.put(Constants.SmsMapKeys.HOUR, Integer.valueOf(hour));
//        map.put(Constants.SmsMapKeys.MINUTE, Integer.valueOf(minute));

        return result;
//        return map;
    }
}
