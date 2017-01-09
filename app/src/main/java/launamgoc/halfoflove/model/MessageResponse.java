package launamgoc.halfoflove.model;

import java.util.List;

/**
 * Created by Admin on 1/10/2017.
 */

public class MessageResponse {
    public String getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(String multicast_id) {
        this.multicast_id = multicast_id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    private String multicast_id;
    private String success;

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    private String failure;

    public String getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(String canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public List<MessageResponseResult> getResults() {
        return results;
    }

    public void setResults(List<MessageResponseResult> results) {
        this.results = results;
    }

    private String canonical_ids;
    private List<MessageResponseResult> results;
}
