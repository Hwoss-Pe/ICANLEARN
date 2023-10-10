package com.utils;

import com.pojo.ChatRecords;

import java.util.Comparator;

public class TimestampComparator implements Comparator<ChatRecords> {
    @Override
    public int compare(ChatRecords record1, ChatRecords record2) {
        return record1.getTimestamp().compareTo(record2.getTimestamp());
    }
}