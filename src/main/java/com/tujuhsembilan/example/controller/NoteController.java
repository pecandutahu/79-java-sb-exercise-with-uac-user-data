package com.tujuhsembilan.example.controller;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.example.controller.dto.NoteDto;
import com.tujuhsembilan.example.model.Note;
import com.tujuhsembilan.example.repository.NoteRepo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

  private final NoteRepo repo;

  private final ModelMapper mdlMap;

  @GetMapping
  public ResponseEntity<?> getNotes() {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity
        .ok(repo.findByOwner(username)
            .stream()
            .map(o -> mdlMap.map(o, NoteDto.class))
            .collect(Collectors.toSet()));
  }

  @PostMapping
  public ResponseEntity<?> saveNote(@RequestBody NoteDto body) {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    var newNote = mdlMap.map(body, Note.class);
    newNote.setOwner(username);
    newNote = repo.save(newNote);
    return ResponseEntity.status(HttpStatus.CREATED).body(newNote);
  }

}
