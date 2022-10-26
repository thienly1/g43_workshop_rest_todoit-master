package se.lexicon.todo_it_api.exception;

public class DuplicateException extends RuntimeException{

    public DuplicateException(String message){
        super(message);
    }
}
