package com.zeero.todo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TodoUtil {
    public static ResponseEntity<Object> buildSuccessResponse(Object response) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "SUCCESS");
        data.put("data", response);
        return ResponseEntity.ok(data);
    }
}
