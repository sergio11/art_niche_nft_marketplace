package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SaveUserInfoMapper: IOneSideMapper<UserInfo, SaveUserDTO> {

    override fun mapInToOut(input: UserInfo): SaveUserDTO = with(input) {
        SaveUserDTO(
            uid = uid,
            name = name,
            professionalTitle = professionalTitle,
            walletAddress = walletAddress,
            info = info,
            tags = tags,
            contact = contact,
            photoUrl = photoUrl,
            birthdate = birthdate,
            externalAuthProvider = externalProviderAuthType?.name,
            location = location,
            country = country,
            instagramNick = instagramNick,
            isPublicProfile = preferences?.isPublicProfile,
            showSellingTokensRow = preferences?.showSellingTokensRow,
            showLastTransactionsOfTokens = preferences?.showLastTransactionsOfTokens,
            allowPublishComments = preferences?.allowPublishComments,
            showAccountBalance = preferences?.showAccountBalance
        )
    }

    override fun mapInListToOutList(input: Iterable<UserInfo>): Iterable<SaveUserDTO> =
        input.map(::mapInToOut)

}