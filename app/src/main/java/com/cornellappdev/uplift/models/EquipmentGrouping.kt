package com.cornellappdev.uplift.models

/** An [EquipmentGrouping] is a grouping of one or more pieces of gym equipment under a particular
 * category. [Gym] objects may have multiple [EquipmentGrouping]s to specify all the equipment
 * they carry. */
data class EquipmentGrouping(
    /** The title of this equipment grouping. (e.g. "Cardio Machines") */
    val name: String,
    /** A list of equipment that this grouping offers.
     *
     * Example: The list (("Treadmills", 5), ("Rowing Machines", 3)) indicates that this grouping has
     * 5 treadmills and 3 rowing machines.
     * */
    val equipmentList: List<Pair<String, Int>>
)