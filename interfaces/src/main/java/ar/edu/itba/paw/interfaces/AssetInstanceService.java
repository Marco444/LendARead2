package ar.edu.itba.paw.interfaces;
//import models.assetExistanceContext.interfaces.AssetInstance;
//import models.assetExistanceContext.interfaces.Book;

import ar.edu.itba.paw.models.assetExistanceContext.interfaces.AssetInstance;

import java.util.HashMap;
import java.util.List;

public interface AssetInstanceService {
    HashMap<String, String> getAssetInstanceDisplay(String id);

    public List<AssetInstance> getAllAssetsInstances();
}