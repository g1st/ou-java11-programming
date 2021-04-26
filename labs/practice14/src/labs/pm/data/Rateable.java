package labs.pm.data;

@FunctionalInterface
public interface Rateable<T> {
    Rating DEFAULT_RATING = Rating.NOT_RATED;

    // public abstract might be omitted as that is default behaviour of interfaces
    T applyRating(Rating newRating);

    default T applyRating(int stars) {
        return applyRating(convert(stars));
    }

    default Rating getRating() {
        return DEFAULT_RATING;
    }


    static Rating convert(int stars) {
        if (stars < 0 || stars > Rating.values().length - 1) {
            return DEFAULT_RATING;
        }
        return Rating.values()[stars];
    }

}
