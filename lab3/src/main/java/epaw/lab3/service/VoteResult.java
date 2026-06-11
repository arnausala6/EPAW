package epaw.lab3.service;

public class VoteResult {

    private final boolean success;
    private final String error;
    private final int upvotes;
    private final int downvotes;
    private final int votes;
    private final Integer userVote;

    private VoteResult(boolean success, String error, int upvotes, int downvotes, int votes, Integer userVote) {
        this.success = success;
        this.error = error;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.votes = votes;
        this.userVote = userVote;
    }

    public static VoteResult error(String error) {
        return new VoteResult(false, error, 0, 0, 0, null);
    }

    public static VoteResult ok(int upvotes, int downvotes, int votes, Integer userVote) {
        return new VoteResult(true, null, upvotes, downvotes, votes, userVote);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public int getVotes() {
        return votes;
    }

    public Integer getUserVote() {
        return userVote;
    }
}
