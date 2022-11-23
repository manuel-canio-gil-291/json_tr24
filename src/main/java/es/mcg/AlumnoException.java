package es.mcg;

public class AlumnoException extends Exception{
    public AlumnoException()
    {
        super();
    }

    public AlumnoException(String message) {
        super(message);
    }
    
    public AlumnoException(String message, Throwable exception)
    {
        super(message, exception);
    }
}
