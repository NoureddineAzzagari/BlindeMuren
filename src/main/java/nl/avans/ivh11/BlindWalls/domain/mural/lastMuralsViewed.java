package nl.avans.ivh11.BlindWalls.domain.mural; /**
 * Created by valiant on 10/03/2017.
 */
import java.util.ArrayList;
import java.util.List;

public class lastMuralsViewed {
    public List<Mural> mementoList = new ArrayList<Mural>();

    public void add(Mural state){
        mementoList.add(state);
    }

    public Mural get(int index){
        return mementoList.get(index);
    }
}