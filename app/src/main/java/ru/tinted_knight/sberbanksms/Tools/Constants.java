package ru.tinted_knight.sberbanksms.Tools;

import java.util.HashSet;
import java.util.Set;

public final class Constants {

    public static final String LOG_TAG = "sberLog";

    public static final class RequestCodes {
        public static final int Permissions = 1;
        public static final int AddAgent = 2;
    }

    public static final class Flag {
        public static final String AliasFilter = "AliasFilter";
        public static final String AliasId = "AliasId";
        public static final String AgentId = "AgentId";
        public static final String CardFilter = "Cards";
    }

    public static final String BroadcastIncomeSms = "ru.tinted_knight.sberbanksms.broadcastincomesms";

    public static final String DeviceSmsUri = "content://sms/inbox";

    public static final int DebugSMSLimit = 50;

    public static final String SBER_PHONE_NUMBER = "900";

    public static final String pattern =
            "(\\w{4}\\d{4}\\s)" +                // 1. card number
            "(\\d\\d\\.\\d\\d\\.\\d\\d\\s)" +    // 2. date
            "(\\d{1,2}:\\d{1,2})?" +             // 3. time
            "(.*\\s)" +                          // 4. operation type
            "(\\d+\\.?\\d+р)" +                  // 5. summa
            "(.*)" +                             // 6. agent
            "(Баланс:\\s)" +                     // 7. "Баланс"
            "(\\d+\\.?\\d+р)"                  // 8. balance
            ;
    public static final String passwordPattern = "(.*)(ароль)(.*)";

    public static final String inTransactionPattern =
            "(\\w{4}\\d{4}:\\s)" +               // 1. card number
            "(\\d{1,2}:\\d{1,2}:\\d{1,2})" +     // 2. time
            "(.*\\s)" +                          // 3. op type
            "(\\d+\\.?\\d+р\\.)" +               // 4. summa
            "( от отправителя )" +               // 5. "от отправителя"
            "(.*\\.)" +                          // 6. agent
            "( Сообщение: )?" +                  // 7. "Сообщение"
            "(.*)?"                            // 8. текст сообщения
            ;

    public static final String OddPattern =
            "(Сбербанк Онлайн.\\s)" +           // 1. Marker
            "(.*)" +                            // 2. Some description
            "(\\sперевод\\sна\\sсумму\\s)" +    // 3. Another marker
            "(\\d*,\\d*)" +                     // 4. Summa
            "(\\sRUB)";                         // 5. Currency

    public static final String rublePattern =
                    "(\\w{4})" +                        // 0. card type ECMC or VISA
                    "(\\d{4}\\s)" +                     // 1. card number
                    "(\\d\\d\\.\\d\\d\\.\\d\\d\\s)" +   // 2. date
                    "(\\d{1,2}:\\d{1,2})?" +            // 3. time
                    "(.*\\s)" +                         // 4. operation type
                    "(\\d+\\.?\\d*)р" +                 // 5. summa
                    "(.*)" +                            // 6. agent
                    "(Баланс:\\s)" +                    // 7. "Баланс"
                    "(\\d+\\.?\\d+р)"                   // 8. balance
            ;

    public static final String MOBILE_BANK_FLAG = "Мобильного банка";
    public static final String mobileBankPattern =
                    "(\\w{4})" +                        // 0. card type ECMC or VISA
                    "(\\d{4}\\s)" +                     // 1. card number
                    "(\\d\\d\\.\\d\\d\\.\\d\\d\\s)" +   // 2. date
                    "оплата Мобильного банка за (.*)" + // 3.
                    "-(.*\\s)" +                        // 4.
                    "(\\d+\\.?\\d*)р" +                 // 5. summa
                    "(.*)" +                            // 6. agent
                    "(Баланс:\\s)" +                    // 7. "Баланс"
                    "(\\d+\\.?\\d+р)"                   // 8. balance
            ;

    public static final String dollarPattern =
                    "(\\w{4})" +                        // 0. ECMC or VISA
                    "(\\d{4}\\s)" +                     // 1. card number
                    "(\\d\\d\\.\\d\\d\\.\\d\\d\\s)" +   // 2. date
                    "(\\d{1,2}:\\d{1,2})?" +            // 3. time
                    "(.*\\s)" +                         // 4. oper type
                    "(\\d+\\.?\\d*)USD" +               // 5. summa
                    "(.*)" +                            // 6. agent
                    "(Баланс:\\s)" +                    // 7. "Баланс"
                    "(\\d+\\.?\\d+р)"                   // 9. balance
            ;

    public static final String euroPattern =
                    "(\\w{4})" +                        // 0. ECMC or VISA
                    "(\\d{4}\\s)" +                     // 1. card number
                    "(\\d\\d\\.\\d\\d\\.\\d\\d\\s)" +   // 2. date
                    "(\\d{1,2}:\\d{1,2})?" +            // 3. time
                    "(.*\\s)" +                         // 4. oper type
                    "(\\d+\\.?\\d*)EUR" +               // 5. summa
                    "(.*)" +                            // 6. agent
                    "(Баланс:\\s)" +                    // 7. "Баланс"
                    "(\\d+\\.?\\d+р)"                   // 9. balance
            ;

    public static final String smsDateFormat = "dd.MM.yy HH:mm";
    public static final String fullDateFormat = "dd MM yyyy HH mm";
    public static final String shortDateFormat = "dd MMM";
    public static final String shortDateFormatMonth = "MMM";
    public static final String shortDateFormatDay = "dd";
    public static final String DATEFORMAT_TIMEONLY = "HH mm";

    public static final class AppPreferences {
        public static final String FirstRun = "mFirstRun";
    }

    public enum CardType {
        VISA,ECMC,NONE
    }

    public final static class OperationType {

        public static final int INCOME  = 0; // зачисление
        public static final int ATM_OUT = 1; // выдача наличных ч/з банкомат
        public static final int OUTCOME = 2; // покупка
        public static final int DEBUG = 3; // покупка

        //todo: возможны проблемы с сортировкой
//        public static final String[] OUTCOME_SET = new String[] { "оплата услуг", "покупка", "списание" };
        public static final Set<String> OUTCOME_SET = new HashSet<>();
        static {
            OUTCOME_SET.add("оплата услуг");
            OUTCOME_SET.add("оплата");
            OUTCOME_SET.add("покупка");
            OUTCOME_SET.add("списание");
        }
//        public static final String[] INCOME_SET = new String[] { "зачисление", "зачисление зарплаты" };
        public static final Set<String> INCOME_SET = new HashSet<>();
        static {
            INCOME_SET.add("зачисление зарплаты");
            INCOME_SET.add("зачисление");
        }
        public static final String ATM_OUT_STRING = "выдача наличных";
        public static final String DEBUG_STRING = "debug string";
        public static final Set<String> ATM_OUT_SET = new HashSet<>();
        static {
            ATM_OUT_SET.add("выдача наличных");
            ATM_OUT_SET.add("выдача");
        }

        public static final int ERRORCODE = -1;

        public static int getType(String typeString) {
//            switch (typeString.toLowerCase()) {
//                case OperationType.DEBUG_STRING:    return OperationType.DEBUG;
//            }
            if (OperationType.DEBUG_STRING.equals(typeString.toLowerCase()))
                return OperationType.DEBUG;
//            if (Arrays.binarySearch(OperationType.OUTCOME_SET, typeString) >= 0)
//                return OperationType.OUTCOME;
//            if (Arrays.binarySearch(OperationType.INCOME_SET, typeString) >= 0)
//                return OperationType.INCOME;
            if (OUTCOME_SET.contains(typeString.toLowerCase()))
                return OperationType.OUTCOME;
            if (INCOME_SET.contains(typeString.toLowerCase()))
                return OperationType.INCOME;
            if (ATM_OUT_SET.contains(typeString))
                return OperationType.ATM_OUT;
            return OperationType.ERRORCODE;
        }

//        public static String getString(final int i){
//            switch (i){
//                case INCOME:
//                    return INCOME_STRING_2;
//                case OUTCOME:
//                    return OUTCOME_STRING;
//                case ATM_OUT:
//                    return ATM_OUT_STRING;
//                case DEBUG:
//                    return DEBUG_STRING;
//            }
//            return null;
//        }
    }

    public final static class SmsMapKeys {

        public static final String YEAR = "year";
        public static final String MONTH = "month";
        public static final String DAY = "day";
        public static final String HOUR = "hour";
        public static final String MINUTE = "minute";

    }
}
