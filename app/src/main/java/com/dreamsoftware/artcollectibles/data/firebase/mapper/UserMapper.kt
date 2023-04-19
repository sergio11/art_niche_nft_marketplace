package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class UserMapper: IOneSideMapper<Map<String, Any?>, UserDTO> {

    private companion object {
        const val UID_KEY = "uid"
        const val NAME_KEY = "name"
        const val PROFESSIONAL_TITLE_KEY = "professionalTitle"
        const val INFO_KEY = "info"
        const val WALLET_ADDRESS_KEY = "walletAddress"
        const val CONTACT_KEY = "contact"
        const val PHOTO_KEY = "photo"
        const val BIRTHDATE_KEY = "birthdate"
        const val EXTERNAL_AUTH_PROVIDER_KEY = "externalProviderAuth"
        const val LOCATION_KEY = "location"
        const val TAGS_KEY = "tags"
        const val COUNTRY_KEY = "country"
    }

    override fun mapInToOut(input: Map<String, Any?>): UserDTO = with(input) {
        UserDTO(
            uid = get(UID_KEY) as String,
            name = get(NAME_KEY) as String,
            professionalTitle = get(PROFESSIONAL_TITLE_KEY) as? String,
            info = get(INFO_KEY) as? String,
            walletAddress = get(WALLET_ADDRESS_KEY) as String,
            contact = get(CONTACT_KEY) as? String,
            photoUrl = get(PHOTO_KEY) as? String,
            birthdate = get(BIRTHDATE_KEY) as? String,
            externalProviderAuth = get(EXTERNAL_AUTH_PROVIDER_KEY) as? String,
            location = get(LOCATION_KEY) as? String,
            country = get(COUNTRY_KEY) as? String,
            tags = (get(TAGS_KEY) as? String)?.split(","),
        )
    }

    override fun mapInListToOutList(input: Iterable<Map<String, Any?>>): Iterable<UserDTO> =
        input.map(::mapInToOut)

}