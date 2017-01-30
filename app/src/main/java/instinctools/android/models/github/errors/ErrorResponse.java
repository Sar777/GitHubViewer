package instinctools.android.models.github.errors;

public class ErrorResponse {
    private String mMessage;
    private String mDocumentationUrl;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getDocumentationUrl() {
        return mDocumentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.mDocumentationUrl = documentationUrl;
    }
}
