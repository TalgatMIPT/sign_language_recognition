package com.future_prospects.mike.signlanguagerecognition.model;

/**
 * Модель изобржения, которого будем передовать на сервер.
 */
public class ProcessImage {

    private byte[] data;

    public ProcessImage() {}

    public ProcessImage(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
