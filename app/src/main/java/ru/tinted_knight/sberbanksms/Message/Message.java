package ru.tinted_knight.sberbanksms.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Constants.CardType;
import ru.tinted_knight.sberbanksms.Tools.Constants.SmsMapKeys;

import static ru.tinted_knight.sberbanksms.Tools.Constants.OperationType;

public class Message {

//    private String raw;
    private long _id;
    private String cardNumber;
    private CardType mCardType;
    private long date;
    private int type;
    private String agent;
    private Float summa;
    private Float balance;
    private String alias;

    Message (MessageBuilder mb){
        this._id = mb.getId();
        this.cardNumber = mb.getCard();
        this.date = mb.getDate();
        this.type = mb.getType();
        this.mCardType = mb.getCardType();
        this.agent = mb.getAgent();
        this.summa = mb.getSumma();
        this.balance = mb.getBalance();
//        this.raw = mb.getRaw();
        this.alias = mb.getAlias();
    }

    public Message(final String messageBody) throws ParseException {
        this(messageBody, 0);
    }

    public Message(final String messageBody, final long receiveDate) throws ParseException {
//        this.raw = messageBody;
        this.alias = null;
        Pattern pattern;
        // проверка на "нормальный" паттерн
        if (tryExtractData(messageBody, Pattern.compile(Constants.newPattern))
                || tryExtractData(messageBody, Pattern.compile(Constants.dollarPattern))
                || tryExtractData(messageBody, Pattern.compile(Constants.euroPattern))){
            // nothing
        }
        else {
            pattern = Pattern.compile(Constants.passwordPattern);
            Matcher matcher = pattern.matcher(messageBody);
            if (matcher.find()) {
                // есть слово "пароль" в тексте - не делаем ничего
                this.cardNumber = null;
                this.mCardType = CardType.NONE;
                this.date = 0;
                this.agent = null;
                this.summa = null;
                this.balance = null;
                this.type = OperationType.ERRORCODE;
            }
            else {
                pattern = Pattern.compile(Constants.inTransactionPattern);
                matcher = pattern.matcher(messageBody);
                if (matcher.find()) {
                    // перевод средств между картами входящий
                    this.cardNumber  = matcher.group(1).trim();
                    this.mCardType = CardType.NONE;
                    String typeString = matcher.group(3).trim();
//                    type = calculateType(typeString);
                    type = OperationType.DEBUG;
                    String sumString = matcher.group(4).trim();
                    this.summa = Float.valueOf(sumString.substring(0, sumString.length()-2));
                    this.agent = matcher.group(6).trim();
                    this.balance = 0f;
                    this.date = receiveDate / 1_000;
                }
                else {
                    this.cardNumber = "0000";
                    this.mCardType = CardType.NONE;
                    this.agent = "";
                    this.type = OperationType.DEBUG;
                    this.balance = 0f;
                    this.summa = 0f;
                    this.date = receiveDate / 1_000;
                }
            }
        }
    }

    private boolean tryExtractData(String data, Pattern pattern) throws ParseException {
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            try {
                mCardType = CardType.valueOf(matcher.group(1).trim().toUpperCase());
            }
            catch (IllegalArgumentException e) {
                mCardType = CardType.NONE;
            }
            this.cardNumber  = matcher.group(2).trim();

            String dateString;
            if (matcher.group(4) != null) { // 3-ей группы может не быть
                dateString = matcher.group(3).trim() + " " + matcher.group(4).trim();
            }
            else {
                dateString = matcher.group(3).trim() + " 00:00";
            }
            DateFormat dateFormat = new SimpleDateFormat(Constants.smsDateFormat, Locale.getDefault());
            date = dateFormat.parse(dateString).getTime() / 1000;

            this.agent = matcher.group(7).trim();

            String typeString = matcher.group(5).trim();
            type = OperationType.getType(typeString);

            String sumString = matcher.group(6).trim();
            this.summa = Float.valueOf(sumString.substring(0, sumString.length()));

            String balanceString = matcher.group(9).trim();
            this.balance = Float.valueOf(balanceString.substring(0, balanceString.length()-1));

            return true;
        }
        return false;
    }

    public Float getBalance() {
        return balance;
    }

    public String getCard() {
        return cardNumber;
    }

    public String getAgent() {
        return agent;
    }

    public Float getSumma() {
        return summa;
    }

    public int getType() {
        return type;
    }

//    public String getTypeString() {
//        return OperationType.getString(type);
//    }

    public String getTimeOnly() {
        return new SimpleDateFormat(Constants.DATEFORMAT_TIMEONLY, Locale.getDefault())
                .format(new Date(date * 1_000));
    }

    public String getDateFull() {
        return SimpleDateFormat.getInstance().format(new Date(date * 1_000));
//        return new SimpleDateFormat("yyyy"/,*Constants.fullDateFormat*/)
//                .format(new Date(date * 1_000));
    }

    public String getDateShort(){
        return new SimpleDateFormat(Constants.shortDateFormat, Locale.getDefault())
                .format(new Date(date * 1_000));
    }

    public int getMonth() {
        Date dateObject = new Date(date * 1_000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        sdf.applyPattern("MM");
        String month = sdf.format(dateObject);
        return Integer.valueOf(month);
    }

    public int getYear() {
        Date dateObject = new Date(date * 1_000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        sdf.applyPattern("yyyy");
        String year = sdf.format(dateObject);
        return Integer.valueOf(year);
    }

    public HashMap<String, Integer> getDateSplit(){
        // 0 - 2017y, 1 - 01m, 2 - 16d, 3 - 10h, 4 - 15m
        HashMap<String, Integer> map = new HashMap<>();
        Date dateObject = new Date(date * 1_000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());

        sdf.applyPattern("yyyy");
        String year = sdf.format(dateObject);
//        Slog.log("year is " + year);
        map.put(SmsMapKeys.YEAR, Integer.valueOf(year));

        sdf.applyPattern("MM");
        String month = sdf.format(dateObject);
//        Slog.log("month is " + month);
        map.put(SmsMapKeys.MONTH, Integer.valueOf(month));

        sdf.applyPattern("dd");
        String day = sdf.format(dateObject);
//        Slog.log("day is " + day);
        map.put(SmsMapKeys.DAY, Integer.valueOf(day));

        sdf.applyPattern("HH");
        String hour = sdf.format(dateObject);
//        Slog.log("hour is " + hour);
        map.put(SmsMapKeys.HOUR, Integer.valueOf(hour));

        sdf.applyPattern("mm");
        String minute = sdf.format(dateObject);
//        Slog.log("minute is " + minute);
        map.put(SmsMapKeys.MINUTE, Integer.valueOf(minute));

        return map;
    }

    public int getDate(){
        return (int) date;
    }

    public String getAlias() {
        if (alias != null) {
            return alias;
        }
        else {
            return "";
        }
    }

    public CardType getCardType() {
        return mCardType;
    }

    public long get_id() {
        return _id;
    }
}
