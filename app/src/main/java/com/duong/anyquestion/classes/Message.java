package com.duong.anyquestion.classes;

import android.text.format.Time;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Message {

    private int messages_id;
    private int conversation_id;
    private String sender;
    private String message;
    private boolean typeImage;
    private String time;

    public Message() {
    }

    public int getMessage_id() {
        return messages_id;
    }

    public void setMessage_id(int message_id) {
        this.messages_id = message_id;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getFrom() {
        return sender;
    }

    public void setFrom(String from) {
        this.sender = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isTypeImage() {
        return typeImage;
    }

    public void setTypeImage(boolean typeImage) {
        this.typeImage = typeImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Message(int conversation_id, String from, String message, boolean typeImage) {
        this.conversation_id = conversation_id;
        this.sender = from;
        this.message = message;
        this.typeImage = typeImage;
        SetTimeNow();
    }


    private void SetTimeNow() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        this.time = today.format("%H:%M");
    }


    public String getHourMin() {
        String hour_min = getTime();
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = format.parse(getTime());
            hour_min = date.getHours() + ":" + date.getMinutes();
        } catch (Exception ignored) {
        }

        return hour_min;
    }

    public String toJSON(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("conversation_id", getConversation_id());
            jsonObject.put("sender", getFrom());
            jsonObject.put("message", getMessage());
            jsonObject.put("typeImage", isTypeImage());
            jsonObject.put("time", getTime());

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


}
