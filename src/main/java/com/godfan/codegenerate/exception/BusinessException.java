package com.godfan.codegenerate.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    /**
     * 错误码
     */
    private final int code;

    /**
     * 构造业务异常
     * @param code 错误码，用于标识具体的业务错误类型
     * @param message 错误消息，描述具体的错误信息
     */
    public BusinessException(int code, String message) {
        super(message); // 调用父类 RuntimeException 的构造函数，传入错误消息
        this.code = code; // 将传入的错误码赋值给当前类的 code 字段
    }

    /**
     * 构造业务异常
     * @param errorCode 错误码枚举对象，包含预定义的错误码和消息
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 调用父类构造函数，使用枚举中定义的消息
        this.code = errorCode.getCode(); // 从枚举对象中获取错误码并赋值给当前类的 code 字段
    }

    /**
     * 构造业务异常
     * @param errorCode 错误码枚举对象，提供基础错误码
     * @param message 自定义错误消息，覆盖枚举中默认的消息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message); // 调用父类构造函数，使用自定义的错误消息
        this.code = errorCode.getCode(); // 从枚举对象中获取错误码并赋值给当前类的 code 字段
    }
}
