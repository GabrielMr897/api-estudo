package br.com.apiEM.exception;

public class RefreshTokenException extends RuntimeException {
  
  public RefreshTokenException(String token, String args) {
    super(String.format("Failed for [%s]: %s", token, args));
  }

  private static final long serialVersionUID = 1L;
}
