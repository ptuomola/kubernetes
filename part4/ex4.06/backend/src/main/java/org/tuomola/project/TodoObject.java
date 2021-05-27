package org.tuomola.project;

public class TodoObject {
  public String todoText;
  public boolean isDone;
  public Integer id;

  public TodoObject(Integer id, String todoText, boolean isDone) {
    this.id = id;
    this.todoText = todoText;
    this.isDone = isDone;
  }
}
