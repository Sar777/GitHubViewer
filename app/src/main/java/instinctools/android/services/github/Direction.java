package instinctools.android.services.github;

public enum Direction {
    ASC("asc"),
    DESC("desc");

    private final String direction;

    private Direction(final String direction) {
        this.direction = direction;
    }
    @Override
    public String toString() {
        return direction;
    }
}
