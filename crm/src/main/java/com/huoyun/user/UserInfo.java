package com.huoyun.user;

public interface UserInfo {

    public long getId();

    public void setId(long id);

    public int getLandscapeId();

    public void setLandscapeId(int landscapeId);

    public Long getEmployeeId();

    public void setEmployeeId(Long employeeId);

    public int getTenantId();

    public void setTenantId(int tenantId);

    public boolean isKeyUser();

    public boolean isServiceUser();

    public boolean isSupportUser();

    public UserInfo getRealCurrentUserInfo();
}
