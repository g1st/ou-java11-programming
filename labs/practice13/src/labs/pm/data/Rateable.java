package labs.pm.data;

@FunctionalInterface
public interface Rateable<T> {
    public static final Rating DEFAULT_RATING = Rating.NOT_RATED;

    // public abstract might be omitted as that is default behaviour of interfaces
    public abstract T applyRating(Rating newRating);

    public default T applyRating(int stars) {
        return applyRating(convert(stars));
    }

    public default Rating getRating() {
        return DEFAULT_RATING;
    }


    public static Rating convert(int stars) {
        if (stars < 0 || stars > Rating.values().length - 1) {
            return DEFAULT_RATING;
        }
        return Rating.values()[stars];
    }

}
