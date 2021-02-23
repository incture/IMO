package com.murphy.taskmgmt.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class FracMessageDecoder implements Decoder.Text<FracMessage> {
    @Override
    public FracMessage decode(String s) throws DecodeException {
        Gson gson = new Gson();
        FracMessage message = gson.fromJson(s, FracMessage.class);
        return message;
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    } 
}
