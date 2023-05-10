package com.zeptolab.zeptolabchatservice.controller;

import com.zeptolab.zeptolabchatservice.repositories.persistence.Message;
import com.zeptolab.zeptolabchatservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @CrossOrigin
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }

}
