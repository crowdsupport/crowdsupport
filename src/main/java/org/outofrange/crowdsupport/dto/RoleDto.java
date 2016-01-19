package org.outofrange.crowdsupport.dto;

import java.util.List;

public class RoleDto extends BaseDto {
    private String name;

    private List<String> permissions;

    private boolean systemRole;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermission() {
        return permissions;
    }

    public void setPermission(List<String> permissions) {
        this.permissions = permissions;
    }

    public boolean isSystemRole() {
        return systemRole;
    }

    public void setSystemRole(boolean systemRole) {
        this.systemRole = systemRole;
    }
}
