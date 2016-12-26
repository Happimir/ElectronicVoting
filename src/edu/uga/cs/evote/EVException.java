package edu.uga.cs.evote;


/**
 * This class represents a basic exception to be thrown and handled by the eVote system.
 */
public class EVException 
    extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * Create a new EVException object.
     * @param message the message explaining the exception
     */
    public EVException( String message )
    {
      super( message );
    }

    /**
     * Create a new EVException object.
     * @param cause the cause of the exception
     */
    public EVException( Throwable cause )
    {
      super( cause );
    }
}
