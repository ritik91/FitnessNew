package com.ritikakhiria.fitnessnew.retrofit;

import retrofit2.Response;

public class NetworkException extends Exception {

    private Response mResponse;

    public NetworkException(Response response) {
        mResponse = response;
    }

    public Response getResponse() {
        return mResponse;
    }
}