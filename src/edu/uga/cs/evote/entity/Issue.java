package edu.uga.cs.evote.entity;

import edu.uga.cs.evote.EVException;

public interface Issue 
    extends BallotItem
{
    /** Return the question of this issue.
     * @return the question of this issue
     */
    public String getQuestion();
    
    /** Set the new question of this issue.
     * @param question the new question of this issue
     */
    public void setQuestion( String question );
    
    /** Return the number of yes votes for this issue.
     * @return the number of yes votes for this issue
     */
    public int getYesCount();
    
    /** Set the new number of yes votes for this issue.
     * @param yesCount the new number of yes votes for this issue
     * @throws EVException in case the new yes vote count is negative
     */
    public void setYesCount( int yesCount ) throws EVException;
    
    /** Return the number of no votes for this issue.
     * @return the number of no votes for this issue
     */
    public int getNoCount();
    
    // noCount is a derived attribute, so it is read-only
    //public void setNoCount( int noCount );

    /** Add one YES vote (increment by one) to the yes votes cast for this Issue.
     */
    public void addYesVote();

    /** Add one NO vote (increment by one) to the yes votes cast for this Issue.
     */
    public void addNoVote();

	public void setBallotItemId(long ballotItemId);
	public long getBallotItemId();
}
