package ru.tinted_knight.sberbanksms.Message;

import ru.tinted_knight.sberbanksms.Tools.Constants;

public class MessageBuilder {

    private String card;
    private Constants.CardType cardType;
    private long date;
    private int type;
    private String agent;
    private Float summa;
    private Float balance;
    private String raw;
    private String alias;
    private long _id;

    public MessageBuilder _id(long id) {
        this._id = id;
        return this;
    }

    public MessageBuilder card(String cardNumber){
        this.card = cardNumber;
        return this;
    }

    public MessageBuilder cardType(Constants.CardType cardType) {
        this.cardType = cardType;
        return this;
    }

    public MessageBuilder date(long date){
        this.date = date;
        return this;
    }

    public MessageBuilder type(int type){
        this.type = type;
        return this;
    }

    public MessageBuilder agent(String agent){
        this.agent = agent;
        return this;
    }

    public MessageBuilder summa(Float sum){
        this.summa = sum;
        return this;
    }

    public MessageBuilder balance(Float balance){
        this.balance = balance;
        return this;
    }

    public MessageBuilder raw(String raw){
        this.raw = raw;
        return this;
    }

    public MessageBuilder alias(String alias){
        this.alias = alias;
        return this;
    }

    public Message build(){
        return new Message(this);
    }

    public String getCard() {
        return card;
    }

    public long getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public String getAgent() {
        return agent;
    }

    public Float getSumma() {
        return summa;
    }

    public Float getBalance() {
        return balance;
    }

    public String getRaw() {
        return raw;
    }

    public String getAlias() {
        return alias;
    }

    public long getId() {
        return _id;
    }

    public Constants.CardType getCardType() {
        return cardType;
    }
}
