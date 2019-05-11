package Model;

import java.util.ArrayList;

//TODO: remove this
public interface DeletableObserver {
    void delete(Deletable d, ArrayList<GameObject> loot);
}
