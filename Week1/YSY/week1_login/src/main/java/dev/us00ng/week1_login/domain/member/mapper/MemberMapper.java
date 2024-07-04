package dev.us00ng.week1_login.domain.member.mapper;

import dev.us00ng.week1_login.domain.member.dto.request.SignupReq;
import dev.us00ng.week1_login.domain.member.dto.response.SignupRes;
import dev.us00ng.week1_login.domain.member.entity.Member;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = ComponentModel.SPRING, imports = {BCryptPasswordEncoder.class, LocalDate.class})
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mappings({
            @Mapping(target = "password", expression = "java(bCryptPasswordEncoder.encode(dto.getPassword()))"),
            @Mapping(target = "createdAt", expression = "java(LocalDate.now())"),
            @Mapping(target = "role", constant = "ROLE_USER"),
            @Mapping(target = "enabled", constant = "false"),
            @Mapping(target="id", ignore = true)
    })
    Member toEntity(SignupReq dto, BCryptPasswordEncoder bCryptPasswordEncoder);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "name", source = "name")
    SignupRes toDto(Member entity);
}
