package com.pubnub.api.endpoints;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNTimeResult;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public class Time extends Endpoint<List<Long>, PNTimeResult> {

    public Time(PubNub pubnub) {
        super(pubnub);
    }

    private interface TimeService {
        @GET("/time/0")
        Call<List<Long>> fetchTime(@QueryMap Map<String, String> options);
    }

    @Override
    protected final void validateParams() throws PubNubException {

    }

    @Override
    protected final Call<List<Long>> doWork(Map<String, String> params) {
        TimeService service = this.createRetrofit().create(TimeService.class);
        return service.fetchTime(params);
    }

    @Override
    protected final PNTimeResult createResponse(final Response<List<Long>> input) throws PubNubException {
        PNTimeResult.PNTimeResultBuilder timeData = PNTimeResult.builder();

        if (input.body() == null || input.body().size() == 0) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        timeData.timetoken(input.body().get(0));
        return timeData.build();
    }

    protected int getConnectTimeout() {
        return this.getPubnub().getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return this.getPubnub().getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNTimeOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return false;
    }

}
