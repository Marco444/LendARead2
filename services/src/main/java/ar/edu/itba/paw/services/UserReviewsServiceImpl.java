package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.AssetInstanceNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.UserAssetInstanceService;
import ar.edu.itba.paw.interfaces.UserReviewsService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.assetLendingContext.implementations.LendingImpl;
import ar.edu.itba.paw.models.userContext.implementations.UserImpl;
import ar.edu.itba.paw.models.userContext.implementations.UserReview;
import ar.edu.itba.paw.models.viewsContext.implementations.PagingImpl;
import ar.itba.edu.paw.persistenceinterfaces.UserDao;
import ar.itba.edu.paw.persistenceinterfaces.UserReviewsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserReviewsServiceImpl implements UserReviewsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserReviewsServiceImpl.class);

    private final UserReviewsDao userReviewsDao;

    private final UserAssetInstanceService userAssetInstanceService;
    private final UserDao userDao;

    private final UserService userService;
    @Autowired
    public UserReviewsServiceImpl(final UserReviewsDao userReviewsDao,final UserDao userDao, final UserAssetInstanceService userAssetInstanceService, final UserService userService){
        this.userReviewsDao = userReviewsDao;
        this.userDao = userDao;
        this.userService = userService;
        this.userAssetInstanceService = userAssetInstanceService;
    }


    @Transactional
    @Override
    public void addReview(UserReview userReview) {
        userReviewsDao.addReview(userReview);
    }

    @Override
    public boolean lenderCanReview(final int lendingId) throws AssetInstanceNotFoundException, UserNotFoundException {
        final LendingImpl lending = userAssetInstanceService.getBorrowedAssetInstance(lendingId);
        return lending.getAssetInstance().getOwner().equals(userService.getUser(userService.getCurrentUser()));
    }

    @Override
    public boolean borrowerCanReview(int lendingId) throws AssetInstanceNotFoundException, UserNotFoundException {
        final LendingImpl lending = userAssetInstanceService.getBorrowedAssetInstance(lendingId);
        return lending.getUserReference().equals(userService.getUser(userService.getCurrentUser()));
    }

    @Transactional
    @Override
    public double getRating(UserImpl user) {
        return userReviewsDao.getRating(user);
    }

    private UserImpl getUser(int userId) throws UserNotFoundException {
        Optional<UserImpl> user = userDao.getUser(userId);
        if(!user.isPresent())
            throw new UserNotFoundException("not found user to get the rating");
        return user.get();
    }

    @Transactional
    @Override
    public double getRatingById(int userId) throws UserNotFoundException {
        return getRating(getUser(userId));
    }

    @Transactional
    @Override
    public PagingImpl<UserReview> getUserReviewsAsLender(int pageNum, int itemsPerPage,UserImpl recipient) {
        return userReviewsDao.getUserReviewsAsLender(pageNum,itemsPerPage,recipient);
    }

    @Transactional
    @Override
    public PagingImpl<UserReview> getUserReviewsAsLenderById(int pageNum, int itemsPerPage,int recipientId) throws UserNotFoundException{
        return getUserReviewsAsLender(pageNum,itemsPerPage,getUser(recipientId));
    }

    @Transactional
    @Override
    public PagingImpl<UserReview> getUserReviewsBorrower(int pageNum, int itemsPerPage, UserImpl reviewer) {
        return userReviewsDao.getUserReviewsAsBorrower(pageNum,itemsPerPage,reviewer);
    }

    @Transactional
    @Override
    public PagingImpl<UserReview> getUserReviewsAsReviewerById(final int pageNum,final int itemsPerPage,int reviewerId) throws UserNotFoundException {
        return getUserReviewsBorrower(pageNum,itemsPerPage,getUser(reviewerId));
    }
}
