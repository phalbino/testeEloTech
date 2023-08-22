package com.testeEloTech.Api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Request {

    private final Map<String, Object> requestObjects = new LinkedHashMap<>();

}
