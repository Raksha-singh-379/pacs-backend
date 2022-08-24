package com.techvg.pacs.service.mapper;

import com.techvg.pacs.domain.SecurityPermission;
import com.techvg.pacs.service.dto.SecurityPermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SecurityPermission} and its DTO {@link SecurityPermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SecurityPermissionMapper extends EntityMapper<SecurityPermissionDTO, SecurityPermission> {}
