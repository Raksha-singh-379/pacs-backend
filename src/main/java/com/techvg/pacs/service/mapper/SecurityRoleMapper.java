package com.techvg.pacs.service.mapper;

import com.techvg.pacs.domain.SecurityPermission;
import com.techvg.pacs.domain.SecurityRole;
import com.techvg.pacs.service.dto.SecurityPermissionDTO;
import com.techvg.pacs.service.dto.SecurityRoleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SecurityRole} and its DTO {@link SecurityRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SecurityRoleMapper extends EntityMapper<SecurityRoleDTO, SecurityRole> {
    @Mapping(target = "securityPermissions", source = "securityPermissions", qualifiedByName = "securityPermissionPermissionNameSet")
    SecurityRoleDTO toDto(SecurityRole s);

    @Mapping(target = "removeSecurityPermission", ignore = true)
    SecurityRole toEntity(SecurityRoleDTO securityRoleDTO);

    @Named("securityPermissionPermissionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "permissionName", source = "permissionName")
    SecurityPermissionDTO toDtoSecurityPermissionPermissionName(SecurityPermission securityPermission);

    @Named("securityPermissionPermissionNameSet")
    default Set<SecurityPermissionDTO> toDtoSecurityPermissionPermissionNameSet(Set<SecurityPermission> securityPermission) {
        return securityPermission.stream().map(this::toDtoSecurityPermissionPermissionName).collect(Collectors.toSet());
    }
}
