package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstance;
import ar.edu.itba.paw.models.assetLendingContext.implementations.Lending;
import ar.edu.itba.paw.models.assetLendingContext.implementations.LendingState;
import ar.edu.itba.paw.models.userContext.implementations.User;
import ar.edu.itba.paw.models.viewsContext.implementations.PagingImpl;

import java.time.LocalDate;
import java.util.List;

public interface AssetAvailabilityService {

    Lending borrowAsset(final int assetId, final String borrower, final LocalDate borrowDate, final LocalDate devolutionDate)  throws AssetInstanceBorrowException, UserNotFoundException, DayOutOfRangeException;

    void changeReservability(final int assetId) throws AssetInstanceNotFoundException, AssetInstanceBorrowException;

    void returnAsset(final int lendingId) throws AssetInstanceNotFoundException, LendingCompletionUnsuccessfulException;

    void confirmAsset(final int lendingId) throws AssetInstanceNotFoundException, LendingCompletionUnsuccessfulException;

    User getLender(final int lendingId) throws AssetInstanceNotFoundException;

    void rejectAsset(final int lendingId) throws AssetInstanceNotFoundException, LendingCompletionUnsuccessfulException;

    void changeLending(final int lendingId,final String state) throws AssetInstanceNotFoundException, LendingCompletionUnsuccessfulException;
    List<Lending> getActiveLendings(final AssetInstance ai);
    PagingImpl<Lending> getPagingActiveLendings(final int page, final int size, final Integer aiId, final Integer borrowerId, final LendingState lendingState, final Integer lenderId);

     void cancelAsset(final int lendingId) throws AssetInstanceNotFoundException, LendingCompletionUnsuccessfulException;

    boolean haveActiveLendings(final AssetInstance ai);
    void notifyNewLendings();
}
