package nl.avans.ivh11.BlindWalls.services.interfaces;

import nl.avans.ivh11.BlindWalls.domain.mural.Mural;
import nl.avans.ivh11.BlindWalls.viewModel.MuralViewModel;
import org.json.JSONException;

import java.util.ArrayList;

public interface IMuralService {

    Iterable<Mural> getAllMurals();
    boolean getMuralsFromDB();
    ArrayList<Mural> getAllMuralsFromAPI() throws JSONException;
    void addMural(Mural mural);
    void deleteMural(Long id);
    void saveEditedMural(Mural mural);
    Mural getMural(Long id);
    Iterable<Mural> searchMural(String name, String searchStrategy);
}
