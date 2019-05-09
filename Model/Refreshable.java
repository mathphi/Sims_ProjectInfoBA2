package Model;

//TODO!!!
public interface Refreshable {
    void attachRefreshableObserver(RefreshableObserver po);

    void notifyRefresh();
}
