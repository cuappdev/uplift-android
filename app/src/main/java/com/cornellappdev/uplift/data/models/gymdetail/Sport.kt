package com.cornellappdev.uplift.data.models.gymdetail

class Sport(val painterId: Int, val name: String) : Comparable<Sport> {
    override fun compareTo(other: Sport): Int {
        return name.compareTo(other.name)
    }
}

