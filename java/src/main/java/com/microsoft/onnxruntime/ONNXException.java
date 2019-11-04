/*
 * Copyright © 2019, Oracle and/or its affiliates. All rights reserved.
 * Licensed under the MIT License.
 */
package com.microsoft.onnxruntime;

/**
 * An exception which contains any error messages out of ONNX.
 */
public class ONNXException extends Exception {
    private static final long serialVersionUID = 1L;

    private final OrtErrorCode errorCode;

    /**
     * Creates an ONNXException with a default Java error code and the specified message.
     * @param message The message to use.
     */
    public ONNXException(String message) {
        super(message);
        this.errorCode = OrtErrorCode.ORT_JAVA_UNKNOWN;
    }

    /**
     * Used to throw an exception from native code as it handles the enum lookup in Java.
     * @param code The error code.
     * @param message The message.
     */
    public ONNXException(int code, String message) {
        this(OrtErrorCode.mapFromInt(code),message);
    }

    /**
     * Creates an ONNXException using the specified error code and message.
     * @param code The error code from the native runtime.
     * @param message The error message.
     */
    public ONNXException(OrtErrorCode code, String message) {
        super("Error code - " + code + " - message: " + message);
        this.errorCode = code;
    }

    /**
     * Return the error code.
     * @return The error code.
     */
    public OrtErrorCode getCode() {
        return errorCode;
    }

    /**
     * Maps the OrtErrorCode struct in "onnxruntime_c_api.h" with an additional entry for Java side errors.
     */
    public enum OrtErrorCode {
        ORT_JAVA_UNKNOWN(-1),
        ORT_OK(0),
        ORT_FAIL(1),
        ORT_INVALID_ARGUMENT(2),
        ORT_NO_SUCHFILE(3),
        ORT_NO_MODEL(4),
        ORT_ENGINE_ERROR(5),
        ORT_RUNTIME_EXCEPTION(6),
        ORT_INVALID_PROTOBUF(7),
        ORT_MODEL_LOADED(8),
        ORT_NOT_IMPLEMENTED(9),
        ORT_INVALID_GRAPH(10),
        ORT_EP_FAIL(11);

        private final int value;

        private static final OrtErrorCode[] values = new OrtErrorCode[12];

        static {
            for (OrtErrorCode ot : OrtErrorCode.values()) {
                if (ot != ORT_JAVA_UNKNOWN) {
                    values[ot.value] = ot;
                }
            }
        }

        OrtErrorCode(int value) {
            this.value = value;
        }

        /**
         * Maps from an int in native land into an OrtErrorCode instance.
         * @param value The value to lookup.
         * @return The enum instance.
         */
        public static OrtErrorCode mapFromInt(int value) {
            if ((value >= 0) && (value < values.length)) {
                return values[value];
            } else {
                return ORT_JAVA_UNKNOWN;
            }
        }
    }

}
