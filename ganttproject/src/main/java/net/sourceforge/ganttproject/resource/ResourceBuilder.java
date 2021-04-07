package net.sourceforge.ganttproject.resource;

import java.math.BigDecimal;

public abstract class ResourceBuilder {
  String myName;
  Integer myID;
  String myEmail;
  String myPhone;
  String myRole;
  BigDecimal myStandardRate;

  public ResourceBuilder withName(String name) {
    myName = name;
    return this;
  }

  public ResourceBuilder withID(String id) {
    myID = Integer.valueOf(id);
    return this;
  }

  public ResourceBuilder withID(Integer id) {
    myID = id;
    return this;
  }

  public ResourceBuilder withEmail(String email) {
    myEmail = email;
    return this;
  }

  public ResourceBuilder withPhone(String phone) {
    myPhone = phone;
    return this;
  }

  public ResourceBuilder withRole(String role) {
    myRole = role;
    return this;
  }

  public ResourceBuilder withStandardRate(String rate) {
    if (rate != null) {
      try {
        myStandardRate = new BigDecimal(rate);
      } catch (NumberFormatException e) {
        myStandardRate = null;
      }
    }
    return this;
  }

  public ResourceBuilder withStandardRate(BigDecimal rate) {
    myStandardRate = rate;
    return this;
  }

  public abstract HumanResource build();
}
