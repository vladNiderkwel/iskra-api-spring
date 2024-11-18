package com.niderkvel.iskraapispring.forms

import com.niderkvel.iskraapispring.ROLE_TUTOR

data class StaffForm(
    var name: String,
    var email: String,
    var password: String,
    var role: Byte = ROLE_TUTOR
)