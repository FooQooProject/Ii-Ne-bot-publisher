package com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetListResponse implements Serializable {

    private static final long serialVersionUID = -4621559503100682231L;

    @JsonProperty("statuses")
    List<TweetResponse> statuses = new ArrayList<>();
}


