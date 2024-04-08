package semicolon.MeetOn_Reply.global.logtrace;


public interface LogTrace {

    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}
