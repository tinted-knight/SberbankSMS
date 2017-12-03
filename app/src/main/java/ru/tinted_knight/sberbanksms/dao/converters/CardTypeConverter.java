package ru.tinted_knight.sberbanksms.dao.converters;

import android.arch.persistence.room.TypeConverter;

import ru.tinted_knight.sberbanksms.Tools.Constants;

public class CardTypeConverter {

    @TypeConverter
    public Constants.CardType fromString(String cardTypeString) {
        return Constants.CardType.valueOf(cardTypeString);
    }

}
