package com.guesthouse.wishboard.global;

public record ApiResponsTemplate<T> (
            String status,
            String message,
            T data
    ) {
        public static <T> ApiResponsTemplate<T> success(T data) {
            return new ApiResponsTemplate<>("SUCCESS", null, data);
        }
    }

