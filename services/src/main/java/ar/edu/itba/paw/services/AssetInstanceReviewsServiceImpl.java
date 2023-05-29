package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.AssetInstanceNotFoundException;
import ar.edu.itba.paw.interfaces.AssetInstanceReviewsService;
import ar.edu.itba.paw.interfaces.AssetInstanceService;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstanceImpl;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstanceReview;
import ar.edu.itba.paw.models.assetExistanceContext.interfaces.AssetInstance;
import ar.itba.edu.paw.persistenceinterfaces.AssetInstanceReviewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetInstanceReviewsServiceImpl implements AssetInstanceReviewsService {

    private final AssetInstanceReviewsDao assetInstanceReviewsDao;

    private final AssetInstanceService assetInstanceService;

    @Autowired
    public AssetInstanceReviewsServiceImpl(AssetInstanceReviewsDao assetInstanceReviewsDao, AssetInstanceService assetInstanceService) {
        this.assetInstanceReviewsDao = assetInstanceReviewsDao;
        this.assetInstanceService = assetInstanceService;
    }

    @Override
    public void addReview(AssetInstanceReview assetInstanceReview) {
       assetInstanceReviewsDao.addReview(assetInstanceReview);
    }

    @Override
    public double getRating(AssetInstanceImpl assetInstance) {
        return assetInstanceReviewsDao.getRating(assetInstance);
    }

    @Override
    public double getRatingById(int assetInstanceId) throws AssetInstanceNotFoundException {
        AssetInstanceImpl assetInstance = assetInstanceService.getAssetInstance(assetInstanceId);
        return assetInstanceReviewsDao.getRating(assetInstance);
    }

    @Override
    public List<AssetInstanceReview> getAssetInstanceReviews(AssetInstanceImpl assetInstance) {
        return assetInstanceReviewsDao.getAssetInstanceReviews(assetInstance);
    }

    @Override
    public List<AssetInstanceReview> getAssetInstanceReviewsById(int assetInstanceId) throws AssetInstanceNotFoundException {
        AssetInstanceImpl assetInstance = assetInstanceService.getAssetInstance(assetInstanceId);
        return assetInstanceReviewsDao.getAssetInstanceReviews(assetInstance);
    }
}