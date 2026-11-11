package com.cornellappdev.uplift.data.models.gymdetail

/**
 * A [EquipmentInfo] is a data class that holds information about a piece of gym equipment.
 * @param name the name of the equipment
 * @param quantity the quantity of the equipment
 */
data class EquipmentInfo(
    val name: String,
    val quantity: Int
)

/**
 * A [EquipmentCategory] is a data class that holds information about a category of gym equipment.
 * @param name the name of the category
 * @param equipmentList a list of [EquipmentInfo] objects that hold information about the equipment
 * in this category
 */
data class EquipmentCategory(
    val name: String,
    val equipmentList: List<EquipmentInfo>? = null
)

/**
 * A [GymEquipmentGroupInfo] is a data class that holds information about a group of gym equipment.
 * @param muscleGroupName the name of the muscle group that the equipment in this group targets
 * @param muscleImageId the image id of the muscle group that the equipment in this group targets
 * @param categories a list of [EquipmentCategory] objects that hold information about the
 * equipment in this group
 */
data class GymEquipmentGroupInfo(
    val muscleGroupName: String,
    val muscleImageId: Int,
    val categories: List<EquipmentCategory>
)