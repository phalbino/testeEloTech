package com.testeEloTech.Api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class GenericResponse {

    private Integer status;

    private String message;

    private String details;

    private Timestamp timestamp;

    private Request request;

}
