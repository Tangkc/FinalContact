package com.jc.android.contact.presentation.model;

/**
 * Class that represents a user in the presentation layer.
 */
public class ContactModel {

  private long  id;
  private String  displayName;//显示的名字
  private String  deptId;
  private String  orderNo;
  private String  photo;
  private String  mobile;

  private String loginName;
  private String userName;
  private String email;
  private String dutyId;
  private String deptName;
  private Long orgId;
  private String orgName;
  private String officeTel;/*办公室电话*/
  private String dutyIdValue;/*职务*/

  private String sortLetters;  //显示数据拼音的首字母
  private String formatNum;
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDeptId() {
    return deptId;
  }

  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getSortLetters() {
    return sortLetters;
  }

  public void setSortLetters(String sortLetters) {
    this.sortLetters = sortLetters;
  }

  public String getFormatNum() {
    return formatNum;
  }

  public void setFormatNum(String formatNum) {
    this.formatNum = formatNum;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDutyId() {
    return dutyId;
  }

  public void setDutyId(String dutyId) {
    this.dutyId = dutyId;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getOfficeTel() {
    return officeTel;
  }

  public void setOfficeTel(String officeTel) {
    this.officeTel = officeTel;
  }

  public String getDutyIdValue() {
    return dutyIdValue;
  }

  public void setDutyIdValue(String dutyIdValue) {
    this.dutyIdValue = dutyIdValue;
  }
}
