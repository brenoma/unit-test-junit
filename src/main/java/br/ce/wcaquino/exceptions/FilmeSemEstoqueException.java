package br.ce.wcaquino.exceptions;

public class FilmeSemEstoqueException extends Exception{

    private static final long serialVersionUID = 3837982804180390821L;

    public FilmeSemEstoqueException(String message) {
        super(message);
    }
}
