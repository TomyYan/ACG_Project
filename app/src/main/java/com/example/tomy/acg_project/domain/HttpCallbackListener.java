package com.example.tomy.acg_project.domain;

import org.json.JSONException;

/**
 * Created by tomy on 18-3-9.
 */
public interface HttpCallbackListener {
    void onFinish(String response) throws JSONException;
    void onError(Exception e);
}
