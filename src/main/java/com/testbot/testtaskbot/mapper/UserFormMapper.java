package com.testbot.testtaskbot.mapper;

import com.testbot.testtaskbot.dto.UserForm;
import com.testbot.testtaskbot.model.Form;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserFormMapper {

    Form mapToEntity(UserForm userForm);

    UserForm mapFromEntity(Form entity);
}
