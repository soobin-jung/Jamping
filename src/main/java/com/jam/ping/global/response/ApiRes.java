package com.jam.ping.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collection;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRes<T> {

    private Integer code = 100;

    private Integer count;

    private Long resultId;

    private T data;

    private String message;

    /**
     * 단건 또는 목록 조회 응답 데이터를 설정합니다.
     */
    public ApiRes<T> successData(T data) {
        if (data == null) {
            this.code = 101;
        }

        this.data = data;

        if (data instanceof Collection<?> collection) {
            this.count = collection.size();
        }

        return this;
    }

    /**
     * 단일 CUD 이벤트 결과 ID를 설정합니다.
     */
    public ApiRes<T> manipulationOne(Long id) {
        this.resultId = id;
        return this;
    }

    /**
     * 다중 CUD 이벤트 결과 건수를 설정합니다.
     */
    public ApiRes<T> manipulationList(Long count) {
        this.count = count == null ? null : count.intValue();
        return this;
    }

    /**
     * 기본 성공 메시지를 설정합니다.
     */
    public ApiRes<T> responseMsg(String msg) {
        this.message = msg;
        return this;
    }

    /**
     * 코드와 메시지를 함께 설정합니다.
     */
    public ApiRes<T> responseMsg(Integer code, String msg) {
        this.code = code;
        this.message = msg;
        return this;
    }
}
