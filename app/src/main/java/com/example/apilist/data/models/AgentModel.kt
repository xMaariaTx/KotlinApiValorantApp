package com.example.apilist.data.models

data class AgentModel(
    val assetPath: String,
    val background: String,
    val backgroundGradientColors: List<String>,
    val bustPortrait: String,
    val characterTags: List<String>,
    val description: String,
    val developerName: String,
    val displayIcon: String,
    val displayIconSmall: String,
    val displayName: String,
    val fullPortrait: String,
    val fullPortraitV2: String,
    val homeScreenPromoTileImage: String,
    val isAvailableForTest: Boolean,
    val isBaseContent: Boolean,
    val isFullPortraitRightFacing: Boolean,
    val isPlayableCharacter: Boolean,
    val minimapPortrait: String,
    val releaseDate: String,
    val uuid: String,
    val role: Role,
    val voiceLine: Any
)

data class Role(
    val uuid: String,
    val displayName: String,
    val description: String,
    val displayIcon: String,
    val assetPath: String
)