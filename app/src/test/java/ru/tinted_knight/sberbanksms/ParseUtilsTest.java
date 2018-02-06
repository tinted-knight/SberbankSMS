package ru.tinted_knight.sberbanksms;

import org.junit.Test;

import ru.tinted_knight.sberbanksms.dao.ParseUtils;
import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;

import static org.junit.Assert.assertEquals;

public class ParseUtilsTest {

    @Test
    public void testFromStringToEntity_service_payment_yandex(){
        String message = "ECMC6824 26.10.17 20:10 оплата услуг 2600р OOO NKO YANDEKS.DENGI Баланс: 190369.36р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1509037800L;
        result.type = 2;
        result.agent = "OOO NKO YANDEKS.DENGI";
        result.summa = 2600;
        result.balance = 190369.36f;
        result.year = 2017;
        result.month = 10;
        result.day = 26;
        result.hour = 20;
        result.minute = 10;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

    @Test
    public void testFromStringToEntity_atm_out(){
        String message = "ECMC6824 27.10.17 13:16 выдача наличных 900р ATM 233428 Баланс: 189469.36р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1509099360L;
        result.type = 1;
        result.agent = "ATM 233428";
        result.summa = 900;
        result.balance = 189469.36f;
        result.year = 2017;
        result.month = 10;
        result.day = 27;
        result.hour = 13;
        result.minute = 16;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

    @Test
    public void testFromStringToEntity_service_payment_mobile(){
        String message = "ECMC6824 27.10.17 21:35 оплата услуг 50р BEELINE Баланс: 189419.36р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1509129300L;
        result.type = 2;
        result.agent = "BEELINE";
        result.summa = 50;
        result.balance = 189419.36f;
        result.year = 2017;
        result.month = 10;
        result.day = 27;
        result.hour = 21;
        result.minute = 35;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

    @Test
    public void testFromStringToEntity_income_salary(){
        String message = "ECMC6824 31.10.17 12:33 зачисление зарплаты 87423.92р Баланс: 276643.28р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1509442380L;
        result.type = 0;
        result.agent = "";
        result.summa = 87423.92f;
        result.balance = 276643.28f;
        result.year = 2017;
        result.month = 10;
        result.day = 31;
        result.hour = 12;
        result.minute = 33;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

    @Test
    public void testFromStringToEntity_outcome_buying(){
        String message = "ECMC6824 01.11.17 14:58 покупка 119р PYATEROCHKA 5367 Баланс: 275345.28р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1509537480L;
        result.type = 2;
        result.agent = "PYATEROCHKA 5367";
        result.summa = 119;
        result.balance = 275345.28f;
        result.year = 2017;
        result.month = 11;
        result.day = 1;
        result.hour = 14;
        result.minute = 58;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

    @Test
    public void testFromStringToEntity_outcome_sbrf(){
        String message = "ECMC6824 07.11.17 20:14 оплата 399.40р Баланс: 270748.88р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1510074840L;
        result.type = 2;
        result.agent = "";
        result.summa = 399.40f;
        result.balance = 270748.88f;
        result.year = 2017;
        result.month = 11;
        result.day = 7;
        result.hour = 20;
        result.minute = 14;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

    @Test
    public void testFromStringToEntity_income_transfer_sbrf(){
        String message = "ECMC6824 24.11.17 18:24 зачисление 10000р Баланс: 271538.37р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1511537040L;
        result.type = 0;
        result.agent = "";
        result.summa = 10000;
        result.balance = 271538.37f;
        result.year = 2017;
        result.month = 11;
        result.day = 24;
        result.hour = 18;
        result.minute = 24;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

    @Test
    public void testFromStringToEntity_outcome_annual_payment(){
        String message = "ECMC6824 18.01.18 02:25 оплата годового обслуживания карты 100р Баланс: 153404.13р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1516231500L;
        result.type = 2;
        result.agent = "Annual payment";
        result.summa = 100;
        result.balance = 153404.13f;
        result.year = 2018;
        result.month = 1;
        result.day = 18;
        result.hour = 2;
        result.minute = 25;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

    @Test
    public void testFromStringToEntity_outcome_mobilebank_payment(){
        String message = "ECMC6824 24.12.17 оплата Мобильного банка за 24/12/2017-23/01/2018 60р Баланс: 194380.74р";
        FullMessageEntity result = new FullMessageEntity();
        result.cardType = "ECMC";
        result.card = "6824";
        result.date = 1514062800L;
        result.type = 2;
        result.agent = "Оплата мобильного банка";
        result.summa = 60;
        result.balance = 194380.74f;
        result.year = 2017;
        result.month = 12;
        result.day = 24;
        result.hour = 0;
        result.minute = 0;

        assertEquals(ParseUtils.fromStringToEntity(message), result);
    }

}
