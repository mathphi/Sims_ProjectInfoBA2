package Model;

public interface Refreshable {
    void attachRefreshableObserver(RefreshableObserver po);

    void notifyRefresh();
}
