package com.cykj.pojo;


public class CheckOut {

  private long id;
  private long adminId;
  private String leglePerson;
  private String tele;
  private String identityFront;
  private String identityBack;
  private String qrImg;
  private String certificate;
  private long state;
  private String reason;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getAdminId() {
    return adminId;
  }

  public void setAdminId(long adminId) {
    this.adminId = adminId;
  }


  public String getLeglePerson() {
    return leglePerson;
  }

  public void setLeglePerson(String leglePerson) {
    this.leglePerson = leglePerson;
  }


  public String getTele() {
    return tele;
  }

  public void setTele(String tele) {
    this.tele = tele;
  }


  public String getIdentityFront() {
    return identityFront;
  }

  public void setIdentityFront(String identityFront) {
    this.identityFront = identityFront;
  }


  public String getIdentityBack() {
    return identityBack;
  }

  public void setIdentityBack(String identityBack) {
    this.identityBack = identityBack;
  }


  public String getQrImg() {
    return qrImg;
  }

  public void setQrImg(String qrImg) {
    this.qrImg = qrImg;
  }


  public String getCertificate() {
    return certificate;
  }

  public void setCertificate(String certificate) {
    this.certificate = certificate;
  }


  public long getState() {
    return state;
  }

  public void setState(long state) {
    this.state = state;
  }


  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

}
