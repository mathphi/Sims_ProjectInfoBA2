package Model;

//TODO: remove this
public interface Deletable {
    void attachDeletable(DeletableObserver po);

    void notifyDeletableObserver();
}
