package com.badzianga.todo.exception;

public class InvalidUserException extends RuntimeException {
  public InvalidUserException() {
    super("Invalid user");
  }
}
