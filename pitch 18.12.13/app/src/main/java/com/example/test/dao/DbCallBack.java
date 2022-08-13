package com.example.test.dao;

import java.util.List;

/**
 * This interface outlines the methods to be used as a callback when the database completes the asynchronous tasks
 */
public interface DbCallBack {

    void onSuccess(String msg);

    void onFailure(String value);
}
