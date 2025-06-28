package com.testbot.testtaskbot.service;

import com.testbot.testtaskbot.dto.UserForm;
import com.testbot.testtaskbot.mapper.UserFormMapper;
import com.testbot.testtaskbot.repository.FormRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository repository;
    private final UserFormMapper mapper;

    @Transactional
    public void save(UserForm userForm) {
        log.info("Saving {}", userForm);
        repository.save(mapper.mapToEntity(userForm));
    }

    public List<UserForm> getAll() {
        return repository.findAll().stream()
            .map(mapper::mapFromEntity)
            .toList();
    }
}
