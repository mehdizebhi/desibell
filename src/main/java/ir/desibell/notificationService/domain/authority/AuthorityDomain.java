package ir.desibell.notificationService.domain.authority;

public class AuthorityDomain {
  
    private Long userId;
    private Long roleId;

    public AuthorityDomain() {
    }

    public AuthorityDomain(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
