package nl.avans.ivh11.BlindWalls.services.concrete;

import nl.avans.ivh11.BlindWalls.domain.mural.Mural;
import nl.avans.ivh11.BlindWalls.repository.MuralRepository;
import nl.avans.ivh11.BlindWalls.services.interfaces.IMuralService;
import nl.avans.ivh11.BlindWalls.viewModel.MuralViewModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

@Service
public class MuralService implements IMuralService {

    private static final Logger logger = LoggerFactory.getLogger(MuralService.class);
    private final MuralRepository muralRepository;

    @Autowired
    public MuralService(MuralRepository muralRepository) {
        this.muralRepository = muralRepository;
    }

    @Override
    public Iterable<Mural> getAllMurals() {
        return muralRepository.findAll();
    }

    @Override
    public boolean getMuralsFromDB() {
        return muralRepository.count() > 0;
    }

    @Override
    public ArrayList<Mural> getAllMuralsFromAPI() throws JSONException {
        InputStream inputStream;
        String response = "";

        try {
            URL url = new URL("https://api.blindwalls.gallery/apiv2/murals");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (response.equals("")) {
            return null;
        }

        ArrayList<Mural> murals = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {

            Mural mural = new Mural();
            mural.setId((long) jsonArray.getJSONObject(i).getInt("id"));
            mural.setName(jsonArray.getJSONObject(i).getString("title"));
            mural.setDescription(jsonArray.getJSONObject(i).getString("description"));

            murals.add(mural);
        }

        if (murals != null) {
            muralRepository.save(murals);
        }

        return murals;
    }

    @Override
    public void addMural(Mural mural) {
        muralRepository.save(mural);
    }

    @Override
    public void deleteMural(Long id) {
        muralRepository.delete(id);
    }

    @Override
    public Mural getMural(Long id) {
        return muralRepository.findOne(id);
    }

    @Override
    public void saveEditedMural(Mural mural) {
        Mural m = muralRepository.findOne(mural.getId());
        m.setName(mural.getName());
        m.setDescription(mural.getDescription());
        m.setArtistName(mural.getArtistName());
        //

        muralRepository.save(m);
    }

    private String getStringFromInputStream(InputStream inputStream) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();

    }

}

