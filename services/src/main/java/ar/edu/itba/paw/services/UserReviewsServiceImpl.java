package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.LendingNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.exceptions.UserReviewNotFoundException;
import ar.edu.itba.paw.interfaces.UserAssetInstanceService;
import ar.edu.itba.paw.interfaces.UserReviewsService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.assetLendingContext.implementations.LendingImpl;
import ar.edu.itba.paw.models.assetLendingContext.implementations.LendingState;
import ar.edu.itba.paw.models.userContext.implementations.UserImpl;
import ar.edu.itba.paw.models.userContext.implementations.UserReview;
import ar.edu.itba.paw.models.viewsContext.implementations.PagingImpl;
import ar.edu.itba.paw.utils.HttpStatusCodes;
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

    private final UserService userService;

    @Autowired
    public UserReviewsServiceImpl(final UserReviewsDao userReviewsDao, final UserAssetInstanceService userAssetInstanceService, final UserService userService) {
        this.userReviewsDao = userReviewsDao;
        this.userService = userService;
        this.userAssetInstanceService = userAssetInstanceService;
    }


    @Transactional
    @Override
    public UserReview addReview(final int lendingId, final int recipient, final String review, final int rating) throws  UserNotFoundException, LendingNotFoundException {
        LendingImpl lending = userAssetInstanceService.getBorrowedAssetInstance(lendingId);
        UserImpl reviewerUser = userService.getUser(userService.getCurrentUser());
        UserImpl recipientUser = userService.getUserById(recipient);
        UserReview userReview = new UserReview( review, rating,  reviewerUser,recipientUser, lending);
        userReviewsDao.addReview(userReview);
        LOGGER.info("Review added");
        return userReview;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean lenderCanReview(final int lendingId) throws UserNotFoundException, LendingNotFoundException {
        final LendingImpl lending = userAssetInstanceService.getBorrowedAssetInstance(lendingId);
        boolean hasReview = userHasReview(lendingId, userService.getCurrentUser());

        return !hasReview && lending.getAssetInstance().getOwner().equals(userService.getUser(userService.getCurrentUser())) && lending.getActive().equals(LendingState.FINISHED);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean borrowerCanReview(int lendingId) throws UserNotFoundException, LendingNotFoundException {
        final LendingImpl lending = userAssetInstanceService.getBorrowedAssetInstance(lendingId);
        boolean hasReview = userHasReview(lendingId, userService.getCurrentUser());
        return !hasReview && lending.getUserReference().equals(userService.getUser(userService.getCurrentUser())) && lending.getActive().equals(LendingState.FINISHED);
    }


    @Transactional(readOnly = true)
    @Override
    public boolean userHasReview(int lendingId, String user) {
        Optional<UserReview> review = userReviewsDao.getUserReviewsByLendingIdAndUser(lendingId, user);
        return review.isPresent();
    }

    @Transactional(readOnly = true)
    @Override
    public UserReview getUserReviewAsLender(final int id, int reviewId) throws UserReviewNotFoundException, UserNotFoundException {
        return userReviewsDao.getUserReviewAsLender(userService.getUserById(id).getId(),reviewId).orElseThrow(() -> new UserReviewNotFoundException(HttpStatusCodes.NOT_FOUND));

    }

    @Transactional(readOnly = true)
    @Override
    public UserReview getUserReviewAsBorrower(final int id, int reviewId) throws UserReviewNotFoundException, UserNotFoundException {
        return userReviewsDao.getUserReviewAsBorrower(userService.getUserById(id).getId(),reviewId).orElseThrow(() -> new UserReviewNotFoundException(HttpStatusCodes.NOT_FOUND));
    }


    @Transactional(readOnly = true)
    @Override
    public PagingImpl<UserReview> getUserReviewsAsLender(int pageNum, int itemsPerPage, UserImpl recipient) {
        return userReviewsDao.getUserReviewsAsLender(pageNum, itemsPerPage, recipient);
    }

    @Transactional(readOnly = true)
    @Override
    public PagingImpl<UserReview> getUserReviewsAsLenderById(int pageNum, int itemsPerPage, int recipientId) throws UserNotFoundException {
        return getUserReviewsAsLender(pageNum, itemsPerPage, userService.getUserById(recipientId));
    }

    @Transactional(readOnly = true)
    @Override
    public PagingImpl<UserReview> getUserReviewsAsBorrower(int pageNum, int itemsPerPage, UserImpl recipient) {
        return userReviewsDao.getUserReviewsAsBorrower(pageNum, itemsPerPage, recipient);
    }

    @Transactional(readOnly = true)
    @Override
    public PagingImpl<UserReview> getUserReviewsAsReviewerById(final int pageNum, final int itemsPerPage, int reviewerId) throws UserNotFoundException {
        return getUserReviewsAsBorrower(pageNum, itemsPerPage, userService.getUserById(reviewerId));
    }
}
