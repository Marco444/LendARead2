import {useEffect, useState} from 'react';
import { useTranslation } from 'react-i18next';
import {Link} from "react-router-dom";
import LoadingAnimation from "../LoadingAnimation.tsx";
import ConfirmLendingModal from "../modals/ConfirmLendingModal.tsx";
import RejectLendingModal from "../modals/RejectLendingModal.tsx";
import ConfirmReturnModal from "../modals/ConfirmReturnModal.tsx";
import useAssetInstance from "../../hooks/assetInstance/useAssetInstance.ts";
import useUserLendedBooksOptions from "../../hooks/assetInstance/useUserLendedBooksOptions.ts";

export const isRejected = (lending: any) => {
    return lending === "REJECTED"
}
export const isPublic = (lending: any) => {
    return lending === "PUBLIC"
}
export const isPrivate = (lending: any) => {
    return lending === "PRIVATE"
}
export const isFinished = (lending: string) => {
    return lending === "FINISHED"
}

export const isActive = (lending: string) => {
    return lending === "ACTIVE"
}
export const isDelivered = (lending: string) => {
    return lending === "DELIVERED"
}

function LendedBooksOptions({ asset, canReview, fetchUserAssetDetails }) {
    const {t} = useTranslation();


    const [showConfirmAssetModal, setShowConfirmAssetModal] = useState(false)
    const [showRejectAssetModal, setShowRejectAssetModal] = useState(false)
    const [showReturnAssetModal, setShowReturnAssetModal] = useState(false)
    const {rejectLending, returnLending, confirmLending} = useUserLendedBooksOptions(fetchUserAssetDetails)
    const handleReturnAsset = async () => {
        setShowRejectAssetModal(false)
        await returnLending(asset)
    }
    const handleRejectAsset = async () => {
        setShowRejectAssetModal(false)
        await rejectLending(asset)
    }

    const handleConfirmAsset = async () => {
        setShowConfirmAssetModal(false)
        await confirmLending(asset)
    }


    return (
        <div style={{
            backgroundColor: '#f0f5f0',
            padding: '25px',
            borderRadius: '20px',
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
        }} className="flex-column">
            {!(asset === undefined || asset.lending === undefined) && (
                <div>
                    <h3 >Lended Book Actions</h3>
                    {!isRejected(asset.lending.state) && !isFinished(asset.lending.state) && (
                        <div className="options-menu"
                             style={{width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                            {isActive(asset.lending.state) && (
                                <>
                                    <h6 style={{color: '#7d7c7c', fontWeight: 'bold', textAlign: 'center', width: "60%", margin: "15px 0"}}>
                                        {t('userHomeView.pendingText')}
                                    </h6>
                                    <div style={{display: 'flex', justifyContent: 'center', gap: '10px'}}>
                                        <button id="confirmAssetBtn" className="btn btn-green" onClick={() => setShowConfirmAssetModal(true)}>
                                            {t('userHomeView.confirmBook')}
                                        </button>
                                        <button id="rejectAssetBtn" className="btn btn-red-outline" onClick={() => setShowRejectAssetModal(true)}>
                                            {t('userHomeView.rejectAssetTitle')}
                                        </button>
                                    </div>
                                </>
                            )}
                            {isDelivered(asset.lending.state) && (
                                <>
                                    <h6 style={{color: '#7d7c7c', fontWeight: 'bold', textAlign: 'center', width: "60%", marginTop: "10px"}}>
                                        {t('userHomeView.inProgressText')}
                                    </h6>
                                    <button id="returnAssetBtn" className="btn btn-green"
                                            style={{marginTop: '10px', alignSelf: 'center'}} onClick={() => setShowReturnAssetModal(true)}>
                                        {t('userHomeView.confirmReturn')}
                                    </button>
                                </>
                            )}
                        </div>
                    )}
                    {canReview && (
                        <Link className="btn btn-green mt-3" to="/reviews" style={{alignSelf: 'center'}}>
                            {t('makeReview')}
                        </Link>
                    )}
                    {/* Include modal components here */}
                    {/* <ReturnModal lending={lending} /> */}
                    <ConfirmLendingModal showModal={showConfirmAssetModal}
                                         handleCloseModal={() => setShowConfirmAssetModal(false)}
                                        asset={asset}
                                        handleSubmitModal={handleConfirmAsset} />
                    <RejectLendingModal showModal={showRejectAssetModal}
                                        handleCloseModal={() => setShowRejectAssetModal(false)}
                                        asset={asset}
                                        handleSubmitModal={handleRejectAsset} />
                    <ConfirmReturnModal showModal={showReturnAssetModal}
                                        handleCloseModal={() => setShowReturnAssetModal(false)}
                                        asset={asset}
                                        handleSubmitModal={handleReturnAsset} />
                </div>
            )}
        </div>
    );
}
    export default LendedBooksOptions;