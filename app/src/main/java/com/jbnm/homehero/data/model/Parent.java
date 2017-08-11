package com.jbnm.homehero.data.model;

/**
 * Created by janek on 7/17/17.
 */

public class Parent {
  private String id;
  private String email;
  private String child;

  public Parent() {}

  public Parent(String id, String email) {
    this.id = id;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getChild() {
    return child;
  }

  public void setChild(String child) {
    this.child = child;
  }
}
