package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveCommentDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.util.*

class SaveCommentMapper: IOneSideMapper<SaveCommentDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val COMMENT_KEY = "comment"
        const val USER_UID_KEY = "userUid"
        const val TOKEN_ID_KEY = "tokenId"
        const val CREATED_AT_KEY = "createdAt"
    }

    override fun mapInToOut(input: SaveCommentDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            COMMENT_KEY to comment,
            USER_UID_KEY to userUid,
            TOKEN_ID_KEY to tokenId.toString(),
            CREATED_AT_KEY to Date().time.toString()
        )
    }

    override fun mapInListToOutList(input: Iterable<SaveCommentDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}