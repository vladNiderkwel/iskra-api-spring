package com.niderkvel.iskraapispring.forms

data class NewStaffForm(
    var email: String,
    var oldPassword: String,
    var newPassword: String,
)